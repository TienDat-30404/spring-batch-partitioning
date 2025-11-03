// package com.example.spring_batch.controller;

// import com.example.spring_batch.processor.CustomerItemProcessor;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.JobParameters;
// import org.springframework.batch.core.JobParametersBuilder;
// import org.springframework.batch.core.launch.JobLauncher;
// import org.springframework.http.ResponseEntity;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.web.bind.annotation.*;

// import java.util.HashMap;
// import java.util.Map;

// @Slf4j
// @RestController
// @RequestMapping("/api/batch")
// @RequiredArgsConstructor
// public class BatchController {

//     private final JobLauncher jobLauncher;
//     private final Job importCustomerJob;
//     private final JdbcTemplate jdbcTemplate;

//     @PostMapping("/start")
//     public ResponseEntity<Map<String, Object>> startJob(
//             @RequestParam(defaultValue = "false") boolean simulateError) {
        
//         try {
//             log.info("🎬 Starting batch job with simulateError={}", simulateError);
            
//             // Reset counter
//             CustomerItemProcessor.resetCounter();
            
//             JobParameters jobParameters = new JobParametersBuilder()
//                     .addLong("time", System.currentTimeMillis())
//                     .addString("simulateError", String.valueOf(simulateError))
//                     .toJobParameters();
            
//             jobLauncher.run(importCustomerJob, jobParameters);
            
//             Map<String, Object> response = new HashMap<>();
//             response.put("status", "Job started successfully");
//             response.put("simulateError", simulateError);
            
//             return ResponseEntity.ok(response);
            
//         } catch (Exception e) {
//             log.error("Failed to start job", e);
//             Map<String, Object> error = new HashMap<>();
//             error.put("status", "Failed to start job");
//             error.put("error", e.getMessage());
//             return ResponseEntity.internalServerError().body(error);
//         }
//     }

//     @GetMapping("/status")
//     public ResponseEntity<Map<String, Object>> getStatus() {
//         Map<String, Object> status = new HashMap<>();
        
//         try {
//             Long totalRecords = jdbcTemplate.queryForObject(
//                     "SELECT COUNT(*) FROM customer", Long.class);
            
//             Map<String, Long> statusCount = new HashMap<>();
//             jdbcTemplate.query(
//                     "SELECT status, COUNT(*) as cnt FROM customer GROUP BY status",
//                     (rs) -> {
//                         statusCount.put(rs.getString("status"), rs.getLong("cnt"));
//                     });
            
//             status.put("totalRecords", totalRecords);
//             status.put("statusBreakdown", statusCount);
            
//         } catch (Exception e) {
//             status.put("totalRecords", 0);
//             status.put("error", "Database not initialized");
//         }
        
//         return ResponseEntity.ok(status);
//     }

//     @DeleteMapping("/clear")
//     public ResponseEntity<Map<String, String>> clearData() {
//         try {
//             jdbcTemplate.execute("TRUNCATE TABLE customer");
//             jdbcTemplate.execute("DELETE FROM BATCH_STEP_EXECUTION_CONTEXT");
//             jdbcTemplate.execute("DELETE FROM BATCH_STEP_EXECUTION");
//             jdbcTemplate.execute("DELETE FROM BATCH_JOB_EXECUTION_CONTEXT");
//             jdbcTemplate.execute("DELETE FROM BATCH_JOB_EXECUTION_PARAMS");
//             jdbcTemplate.execute("DELETE FROM BATCH_JOB_EXECUTION");
//             jdbcTemplate.execute("DELETE FROM BATCH_JOB_INSTANCE");
            
//             Map<String, String> response = new HashMap<>();
//             response.put("status", "All data cleared successfully");
//             return ResponseEntity.ok(response);
            
//         } catch (Exception e) {
//             Map<String, String> error = new HashMap<>();
//             error.put("status", "Failed to clear data");
//             error.put("error", e.getMessage());
//             return ResponseEntity.internalServerError().body(error);
//         }
//     }
// }
