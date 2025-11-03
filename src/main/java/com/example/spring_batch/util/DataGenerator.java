// package com.example.spring_batch.util;

// import lombok.extern.slf4j.Slf4j;

// import java.io.BufferedWriter;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.math.BigDecimal;
// import java.math.RoundingMode;
// import java.util.Random;
// // Xóa các import không cần thiết:
// // import java.time.LocalDate;
// // import java.util.concurrent.ThreadLocalRandom; 

// @Slf4j
// public class DataGenerator {

//     private static final String[] FIRST_NAMES = {
//             "Nguyen", "Tran", "Le", "Pham", "Hoang", "Phan", "Vu", "Dang", "Bui", "Do",
//             "Ho", "Ngo", "Duong", "Ly", "John", "Jane", "Michael", "Sarah", "David", "Emma"
//     };

//     private static final String[] LAST_NAMES = {
//             "Van", "Thi", "Minh", "Thanh", "Anh", "Huy", "Linh", "Nam", "Tuan", "Duc",
//             "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis"
//     };

//     private static final String[] CITIES = {
//             "Ho Chi Minh", "Ha Noi", "Da Nang", "Can Tho", "Hai Phong", "Bien Hoa",
//             "New York", "London", "Tokyo", "Singapore", "Seoul", "Bangkok"
//     };

//     private static final String[] COUNTRIES = {
//             "Vietnam", "USA", "UK", "Japan", "Singapore", "Korea", "Thailand"
//     };

//     private static final Random random = new Random();

//     public static void generateFile(String filePath, int numberOfRecords) {
//         log.info("📝 Generating {} records to {}", numberOfRecords, filePath);
        
//         long startTime = System.currentTimeMillis();
        
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//             // SỬA: Xóa 'dateOfBirth' khỏi header
//             writer.write("id,firstName,lastName,email,phone,address,city,country,accountBalance");
//             writer.newLine();
            
//             for (int i = 1; i <= numberOfRecords; i++) {
//                 String firstName = getRandomElement(FIRST_NAMES);
//                 String lastName = getRandomElement(LAST_NAMES);
//                 String email = generateEmail(firstName, lastName, i);
//                 String phone = generatePhone();
//                 String address = generateAddress(i);
//                 String city = getRandomElement(CITIES);
//                 String country = getRandomElement(COUNTRIES);
//                 // ĐÃ XÓA: LocalDate dob = generateDateOfBirth(); 
//                 BigDecimal balance = generateBalance();
                
//                 // TẠO LỖI (20% bản ghi thiếu firstName để test skip)
//                 if (i % 5 == 0) { 
//                     firstName = "";
//                     log.debug("🚨 Error record generated at line: {}", i);
//                 }

//                 // SỬA: Bỏ tham số dob khỏi String.format
//                 writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%.2f",
//                         i, firstName, lastName, email, phone, address, city, country, balance));
//                 writer.newLine();
                
//                 if (i % 100000 == 0) {
//                     log.info("✍️ Generated {} records", i);
//                 }
//             }
            
//             long duration = System.currentTimeMillis() - startTime;
//             log.info("✅ File generation completed in {} ms", duration);
//             log.info("📁 File size: {} MB", getFileSizeMB(filePath));
            
//         } catch (IOException e) {
//             log.error("Error generating file", e);
//             throw new RuntimeException("Failed to generate file", e);
//         }
//     }

//     private static String getRandomElement(String[] array) {
//         return array[random.nextInt(array.length)];
//     }

//     private static String generateEmail(String firstName, String lastName, int id) {
//         String domain = random.nextBoolean() ? "gmail.com" : "example.com";
//         return String.format("%s.%s%d@%s", 
//                 firstName.toLowerCase(), lastName.toLowerCase(), id, domain);
//     }

//     private static String generatePhone() {
//         return String.format("0%d%08d", 
//                 random.nextInt(9) + 1, random.nextInt(100000000));
//     }

//     private static String generateAddress(int number) {
//         return String.format("%d Street %d District %d", 
//                 number, random.nextInt(100) + 1, random.nextInt(12) + 1);
//     }

//     /**
//      * ĐÃ XÓA: Phương thức generateDateOfBirth không còn được sử dụng.
//      */
//     // private static LocalDate generateDateOfBirth() {
//     //     long minDay = LocalDate.of(1950, 1, 1).toEpochDay();
//     //     long maxDay = LocalDate.of(2005, 12, 31).toEpochDay();
//     //     long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
//     //     return LocalDate.ofEpochDay(randomDay);
//     // }

//     private static BigDecimal generateBalance() {
//         double balance = random.nextDouble() * 200000;
//         return BigDecimal.valueOf(balance).setScale(2, RoundingMode.HALF_UP);
//     }

//     private static double getFileSizeMB(String filePath) {
//         try {
//             java.io.File file = new java.io.File(filePath);
//             return file.length() / (1024.0 * 1024.0);
//         } catch (Exception e) {
//             return 0;
//         }
//     }

//     public static void main(String[] args) {
//         // Generate 1 million records (approximately 100-150MB file)
//         String outputPath = "data/input/customerss.csv";
        
//         // Tạo thư mục nếu chưa tồn tại
//         java.io.File directory = new java.io.File("data/input");
//         if (!directory.exists()) {
//             directory.mkdirs();
//         }
        
//         // Bạn có thể thay đổi số lượng records ở đây
//         // int numberOfRecords = 10; // 1 triệu records
//         int numberOfRecords = 100000; // 100k records để test nhanh
        
//         generateFile(outputPath, numberOfRecords);
        
//         log.info("🎉 Data generation completed!");
//         log.info("📁 File location: {}", outputPath);
//     }
// }




package com.example.spring_batch.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Slf4j
public class DataGenerator {

    private static final String[] FIRST_NAMES = {
            "Nguyen", "Tran", "Le", "Pham", "Hoang", "Phan", "Vu", "Dang", "Bui", "Do",
            "Ho", "Ngo", "Duong", "Ly", "John", "Jane", "Michael", "Sarah", "David", "Emma"
    };

    private static final String[] LAST_NAMES = {
            "Van", "Thi", "Minh", "Thanh", "Anh", "Huy", "Linh", "Nam", "Tuan", "Duc",
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis"
    };

    private static final String[] CITIES = {
            "Ho Chi Minh", "Ha Noi", "Da Nang", "Can Tho", "Hai Phong", "Bien Hoa",
            "New York", "London", "Tokyo", "Singapore", "Seoul", "Bangkok"
    };

    private static final String[] COUNTRIES = {
            "Vietnam", "USA", "UK", "Japan", "Singapore", "Korea", "Thailand"
    };

    private static final Random random = new Random();

    /**
     * Tạo dữ liệu TỔNG HỢP vào một file duy nhất, với tỷ lệ lỗi thay đổi theo ID range (mô phỏng Partitioning).
     *
     * Luồng 1 (ID 1 - 25000): 20% lỗi
     * Luồng 2 (ID 25001 - 50000): 0% lỗi
     * Luồng 3 (ID 50001 - 75000): 10% lỗi
     * Luồng 4 (ID 75001 - 100000): 2% lỗi
     * * @param filePath Đường dẫn file output.
     * @param numberOfRecords Tổng số bản ghi cần tạo (ví dụ: 100000).
     */
    public static void generateFileWithVariedErrors(String filePath, int numberOfRecords) {
        log.info("📝 Generating {} total records to {} with varied error rates.", numberOfRecords, filePath);
        
        // Xác định kích thước của mỗi 'vùng' (Partition size = 25,000)
        int partitionSize = numberOfRecords / 4; 

        long startTime = System.currentTimeMillis();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,firstName,lastName,email,phone,address,city,country,accountBalance");
            writer.newLine();

            for (int i = 1; i <= numberOfRecords; i++) {
                int errorPercentage;

                // Xác định tỷ lệ lỗi dựa trên ID range
                if (i <= partitionSize) { // 1 - 25000 (P1)
                    errorPercentage = 20;
                } else if (i <= 2 * partitionSize) { // 25001 - 50000 (P2)
                    errorPercentage = 0;
                } else if (i <= 3 * partitionSize) { // 50001 - 75000 (P3)
                    errorPercentage = 10;
                } else { // 75001 - 100000 (P4)
                    errorPercentage = 2;
                }
                
                String firstName = getRandomElement(FIRST_NAMES);
                String lastName = getRandomElement(LAST_NAMES);
                
                // TẠO LỖI THEO TỶ LỆ ĐƯỢC XÁC ĐỊNH BỞI ID RANGE HIỆN TẠI
                if (random.nextInt(100) < errorPercentage) {
                    firstName = ""; // Mô phỏng lỗi (firstName trống)
                }

                String email = generateEmail(firstName, lastName, i);
                String phone = generatePhone();
                String address = generateAddress(i);
                String city = getRandomElement(CITIES);
                String country = getRandomElement(COUNTRIES);
                BigDecimal balance = generateBalance();

                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%.2f",
                        i, firstName, lastName, email, phone, address, city, country, balance));
                writer.newLine();
                
                if (i % 10000 == 0) {
                    log.info("✍️ Generated {} records. Current error rate for range: {}%", i, errorPercentage);
                }
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ File generation {} completed in {} ms. File size: {} MB", filePath, duration, getFileSizeMB(filePath));

        } catch (IOException e) {
            log.error("Error generating file", e);
            throw new RuntimeException("Failed to generate file", e);
        }
    }

    private static String getRandomElement(String[] array) {
        return array[random.nextInt(array.length)];
    }

    private static String generateEmail(String firstName, String lastName, int id) {
        String domain = random.nextBoolean() ? "gmail.com" : "example.com";
        return String.format("%s.%s%d@%s",
                firstName.toLowerCase(), lastName.toLowerCase(), id, domain);
    }

    private static String generatePhone() {
        return String.format("0%d%08d",
                random.nextInt(9) + 1, random.nextInt(100000000));
    }

    private static String generateAddress(int number) {
        return String.format("%d Street %d District %d",
                number, random.nextInt(100) + 1, random.nextInt(12) + 1);
    }

    private static BigDecimal generateBalance() {
        double balance = random.nextDouble() * 200000;
        return BigDecimal.valueOf(balance).setScale(2, RoundingMode.HALF_UP);
    }

    private static double getFileSizeMB(String filePath) {
        try {
            java.io.File file = new java.io.File(filePath);
            return file.length() / (1024.0 * 1024.0);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        // Tổng số bản ghi là 100,000
        int totalRecords = 10;
        String outputPath = "data/input/customers.csv";

        // Tạo thư mục nếu chưa tồn tại
        java.io.File directory = new java.io.File("data/input");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // GỌI HÀM TẠO 1 FILE DUY NHẤT VỚI LỖI PHÂN VÙNG BÊN TRONG
        generateFileWithVariedErrors(outputPath, totalRecords); 
        
        log.info("🎉 Single combined data generation completed! Total records: {}", totalRecords);
        log.info("➡️ File location: {}", outputPath);
        log.info("ℹ️ File này chứa dữ liệu 100K bản ghi, với tỷ lệ lỗi thay đổi theo từng 25K bản ghi để mô phỏng Partitioning.");
    }
}





