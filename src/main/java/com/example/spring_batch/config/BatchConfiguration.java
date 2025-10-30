package com.example.spring_batch.config;

import com.example.spring_batch.model.Customer;
import com.example.spring_batch.processor.CustomerItemProcessor;
import com.example.spring_batch.partitioner.RangePartitioner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    @Value("${batch.chunk-size}")
    private int chunkSize;

    @Value("${batch.partition-size}")
    private int partitionSize;

    @Value("${batch.input-file}")
    private String inputFile;

    // ===========================================
    // READER - Đọc file CSV (ĐÃ SỬA: Xóa dateOfBirth)
    // ===========================================
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customerReader(
            @Value("#{stepExecutionContext['fromLine']}") Long fromLine,
            @Value("#{stepExecutionContext['toLine']}") Long toLine) {

        log.info("Creating reader for lines {} to {}", fromLine, toLine);

        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerReader")
                .resource(new FileSystemResource(inputFile))
                .linesToSkip(fromLine.intValue())
                .maxItemCount(toLine.intValue() - fromLine.intValue())
                .delimited()
                .delimiter(",")
                // SỬA: Xóa "dateOfBirth" và "createdDate" khỏi danh sách tên cột
                .names("id", "firstName", "lastName", "email", "phone",
                        "address", "city", "country", "accountBalance")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                    {
                        setTargetType(Customer.class);
                    }
                })
                .build();
    }

    // ===========================================
    // PROCESSOR - Xử lý và validate dữ liệu
    // ===========================================
    @Bean
    public CustomerItemProcessor customerProcessor() {
        return new CustomerItemProcessor();
    }

    // ===========================================
    // WRITER - Ghi vào database (ĐÃ SỬA: Xóa date_of_birth và created_date)
    // ===========================================
    @Bean
    public JdbcBatchItemWriter<Customer> customerWriter() {
        // SỬA: Loại bỏ date_of_birth và created_date khỏi tên cột và giá trị
        // String sql = "INSERT INTO customer " +
        //         "(id, first_name, last_name, email, phone, address, city, country, " +
        //         "account_balance, status, processed_by) " +
        //         "VALUES (:id, :firstName, :lastName, :email, :phone, :address, :city, :country, " +
        //         ":accountBalance, :status, :processedBy)";

        String sql = "MERGE INTO customer (id, first_name, last_name, email, phone, address, city, country, " +
                 "account_balance, status, processed_by) " +
                 "KEY (id) " + // <<< Cột khóa duy nhất (Primary Key)
                 "VALUES (:id, :firstName, :lastName, :email, :phone, :address, :city, :country, " +
                 ":accountBalance, :status, :processedBy)";

        return new JdbcBatchItemWriterBuilder<Customer>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(sql)
                .dataSource(dataSource)
                .build();
    }

    // ===========================================
    // WORKER STEP - Xử lý từng partition
    // ===========================================
    @Bean
    public Step workerStep() {
        return new StepBuilder("workerStep", jobRepository)
                .<Customer, Customer>chunk(chunkSize, transactionManager)
                .reader(customerReader(null, null))
                .processor(customerProcessor())
                .writer(customerWriter())
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(3)
                .build();
    }

    // ===========================================
    // PARTITIONER - Chia công việc thành các phần
    // ===========================================
    @Bean
    public RangePartitioner rangePartitioner() {
        return new RangePartitioner(inputFile);
    }

    // ===========================================
    // TASK EXECUTOR - Xử lý song song
    // ===========================================
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(partitionSize);
        executor.setMaxPoolSize(partitionSize);
        executor.setQueueCapacity(partitionSize);
        executor.setThreadNamePrefix("batch-partition-");
        executor.initialize();
        return executor;
    }

    // ===========================================
    // PARTITION HANDLER
    // ===========================================
    @Bean
    public PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(workerStep());
        handler.setTaskExecutor(taskExecutor());
        handler.setGridSize(partitionSize);
        return handler;
    }

    // ===========================================
    // MASTER STEP - Quản lý các partition
    // ===========================================
    @Bean
    public Step masterStep() {
        return new StepBuilder("masterStep", jobRepository)
                .partitioner("workerStep", rangePartitioner())
                .partitionHandler(partitionHandler())
                .build();
    }

    // ===========================================
    // JOB - Công việc chính
    // ===========================================
    @Bean
    public Job importCustomerJob(JobCompletionNotificationListener listener) {
        return new JobBuilder("importCustomerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(masterStep())
                .build();
    }
}