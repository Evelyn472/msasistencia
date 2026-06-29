FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8084

CMD ["java","-jar","target/demo-0.0.1-SNAPSHOT.jar"]