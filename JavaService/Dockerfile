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
    && rm -rf /var/lib/apt/lists/*

COPY . /app

RUN mvn clean package

CMD ["java", "-jar", "target/java-database-service.jar"]