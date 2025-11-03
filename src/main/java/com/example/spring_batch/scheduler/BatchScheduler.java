// package com.example.spring_batch.scheduler;

// import com.example.spring_batch.processor.CustomerItemProcessor;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.JobExecution;
// import org.springframework.batch.core.JobParameters;
// import org.springframework.batch.core.JobParametersBuilder;
// import org.springframework.batch.core.launch.JobLauncher;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.scheduling.annotation.Scheduled;

// @Slf4j
// @Configuration
// @RequiredArgsConstructor
// public class BatchScheduler {

//     private final JobLauncher jobLauncher;
//     private final Job importCustomerJob;

//     /**
//      * Tự động khởi chạy Job Batch.
//      * fixedDelay = 30000ms: Job mới sẽ bắt đầu 30 giây sau khi Job trước đó HOÀN TẤT.
//      */
//     @Scheduled(fixedDelay = 30000) // Chạy mỗi 30 giây sau khi Job trước xong
//     public void runBatchJobAutomatically() {
//         try {
//             log.info("🎬 Tự động khởi động Job Batch sau 30 giây...");

//             // Cần reset counter cho mỗi lần chạy Job mới
//             CustomerItemProcessor.resetCounter();
            
//             // JobParameters phải LUÔN thay đổi để Spring Batch coi là một lần chạy mới.
//             JobParameters jobParameters = new JobParametersBuilder()
//                     .addLong("runTime", System.currentTimeMillis())
//                     .addString("source", "Scheduler") // Đánh dấu nguồn khởi tạo
//                     .toJobParameters();

//             // Khởi chạy Job
//             JobExecution execution = jobLauncher.run(importCustomerJob, jobParameters);
            
//             log.info("✅ Job '{}' kết thúc với trạng thái: {}", 
//                      execution.getJobInstance().getJobName(), 
//                      execution.getStatus());

//         } catch (Exception e) {
//             log.error("🔴 Lỗi khi khởi chạy Job Batch tự động: {}", e.getMessage(), e);
//         }
//     }
// }