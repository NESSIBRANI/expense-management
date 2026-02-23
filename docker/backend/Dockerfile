FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY backend/expense-backend/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]