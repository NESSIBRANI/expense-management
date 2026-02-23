pipeline {
    agent any

    stages {

        stage('Checkout Source Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/NESSIBRANI/expense-management.git'
            }
        }

        stage('Build & Deploy with Docker Compose') {
            steps {
                sh '''
                cd docker
                docker-compose down
                docker-compose up --build -d
                '''
            }
        }
    }
}