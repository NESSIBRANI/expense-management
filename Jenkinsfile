pipeline {
    agent any

    stages {

        stage('Build Backend (Maven in Docker)') {
            steps {
                dir('backend/expense-backend') {
                    sh '''
                    ls -la
                    docker run --rm \
                      -v "$PWD":/app \
                      -v "$HOME/.m2":/root/.m2 \
                      -w /app \
                      maven:3.9.9-eclipse-temurin-17 \
                      mvn clean package -DskipTests
                    '''
                }
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