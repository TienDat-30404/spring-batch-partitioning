package com.example.spring_batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("🚀 ==================================================");
        log.info("🚀 JOB STARTED: {}", jobExecution.getJobInstance().getJobName());
        log.info("🚀 Job ID: {}", jobExecution.getJobId());
        log.info("🚀 Start Time: {}", jobExecution.getStartTime());
        log.info("🚀 ==================================================");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("✅ ==================================================");
            log.info("✅ JOB COMPLETED SUCCESSFULLY");
            // log.info("✅ Duration: {} ms", jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime());
            
            // Đếm số bản ghi đã insert
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customer", Long.class);
            log.info("✅ Total records inserted: {}", count);
            
            // Hiển thị một số bản ghi mẫu
            jdbcTemplate.query("SELECT * FROM customer LIMIT 5",
                    (rs, rowNum) -> {
                        log.info("📋 Sample: {} {} - {} - {}", 
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("status"));
                        return null;
                    });
            
            log.info("✅ ==================================================");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("❌ ==================================================");
            log.error("❌ JOB FAILED");
            log.error("❌ Exit Status: {}", jobExecution.getExitStatus());
            jobExecution.getAllFailureExceptions().forEach(ex -> 
                    log.error("❌ Exception: {}", ex.getMessage()));
            log.error("❌ ==================================================");
        }
    }
}