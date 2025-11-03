# Sử dụng JDK 17 làm Base Image (Hoặc phiên bản JDK của bạn)
FROM eclipse-temurin:21-jdk-jammy
# Thiết lập thư mục làm việc
WORKDIR /app

# Copy file JAR đã được build vào Image. 
# Giả sử tên file JAR của bạn là my-batch-job.jar
COPY target/spring-batch-0.0.1-SNAPSHOT.jar app.jar 

# Thiết lập lệnh chạy khi Container được khởi động
ENTRYPOINT ["java","-jar","/app/app.jar"]