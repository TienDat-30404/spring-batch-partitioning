# Dockerfile.dataflow (Chỉ dùng để build SCDF Server)
# Sử dụng image SCDF Server chính thức làm base
FROM springcloud/spring-cloud-dataflow-server:2.11.3-SNAPSHOT

# Cài đặt Docker CLI 
# (Sử dụng apt-get vì image SCDF Server thường dựa trên Debian/Ubuntu)
USER root
RUN apt-get update && apt-get install -y docker.io \
    && rm -rf /var/lib/apt/lists/*
USER cnb