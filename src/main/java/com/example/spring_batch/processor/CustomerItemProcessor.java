package com.example.spring_batch.processor;

import com.example.spring_batch.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;



@Slf4j
public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    @Value("${batch.simulate-error:false}")
    private boolean simulateError;

    private static long processedCount = 0;

    @Override
    public Customer process(Customer customer) throws Exception {
        processedCount++;

        // DEMO: Simulate error sau khi xử lý 500,000 bản ghi
        if (simulateError && processedCount > 500000) {
            log.error("🔴 SIMULATED ERROR at record: {}", processedCount);
            throw new RuntimeException("Simulated Failure at record " + processedCount);
        }

        // Validate dữ liệu
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
            log.warn("⚠️ Skipping customer with empty firstName: {}", customer.getId());
            return null; // Skip bản ghi này
        }

        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            log.warn("⚠️ Invalid email for customer: {}", customer.getId());
            customer.setEmail("invalid@example.com");
        }

        // Xử lý nghiệp vụ
        customer.setFirstName(normalizeString(customer.getFirstName()));
        customer.setLastName(normalizeString(customer.getLastName()));
        customer.setProcessedBy("BATCH_JOB");

        // Tính toán status dựa trên balance (Đã loại bỏ logic tính tuổi)
        String status = calculateStatus(customer);
        customer.setStatus(status);

        if (processedCount % 10000 == 0) {
            log.info("✅ Processed {} records", processedCount);
        }

        return customer;
    }

    private String normalizeString(String value) {
        if (value == null) return "";
        return value.trim().toUpperCase();
    }

    /**
     * Phương thức được sửa đổi để CHỈ dựa vào AccountBalance, vì dateOfBirth đã bị xóa.
     */
    private String calculateStatus(Customer customer) {
        
        // Nếu không có AccountBalance, trả về INACTIVE
        if (customer.getAccountBalance() == null) {
            return "INACTIVE";
        }

        // Logic dựa trên AccountBalance
        if (customer.getAccountBalance().doubleValue() > 100000) {
            return "VIP";
        } else if (customer.getAccountBalance().doubleValue() > 50000) {
            return "PREMIUM";
        } else {
            // Logic dựa trên tuổi (< 25) đã bị loại bỏ
            return "STANDARD";
        }
    }

    public static void resetCounter() {
        processedCount = 0;
    }
}