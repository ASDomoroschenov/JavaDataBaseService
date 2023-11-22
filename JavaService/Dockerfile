FROM maven:3.8.1-openjdk-17-slim

WORKDIR /app

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    openjfx \
    libcanberra-gtk-module \
    libcanberra-gtk3-module \
    libgl1-mesa-glx \
    libgl1-mesa-dri \
    mesa-utils \
    netcat \
    && rm -rf /var/lib/apt/lists/*

COPY . /app
RUN chmod +x wait-for-it.sh

RUN mvn clean package

CMD ["/app/wait-for-it.sh", "database:5432", "--", "java", "-jar", "target/java-database-service.jar"]
