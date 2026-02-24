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

                docker run --rm \
                  -v /var/run/docker.sock:/var/run/docker.sock \
                  -v "$PWD:/work" \
                  -w /work \
                  docker/compose:latest down

                docker run --rm \
                  -v /var/run/docker.sock:/var/run/docker.sock \
                  -v "$PWD:/work" \
                  -w /work \
                  docker/compose:latest up --build -d
                '''
            }
        }
    }
}