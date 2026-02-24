pipeline {
    agent any

    stages {

        stage('Checkout Source Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/NESSIBRANI/expense-management.git'
            }
        }

        stage('Build Backend (Maven in Docker)') {
            steps {
                sh '''
                docker run --rm \
                  -v "$PWD/backend/expense-backend":/app \
                  -v "$HOME/.m2":/root/.m2 \
                  -w /app \
                  maven:3.9.9-eclipse-temurin-17 \
                  mvn clean package -DskipTests
                '''
            }
        }

        stage('Build & Deploy with Docker Compose') {
            steps {
                sh '''
                cd docker
                docker compose down || true
                docker compose up --build -d
                '''
            }
        }
    }
}