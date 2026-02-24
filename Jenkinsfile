pipeline {
    agent any

    stages {

        stage('Build Backend (Maven Wrapper)') {
            steps {
                dir('backend/expense-backend') {
                    sh '''
                    chmod +x mvnw
                    ./mvnw clean package -DskipTests
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