pipeline {
    agent: any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Employee Service') {
            steps {
                dir('employee-service') {
                    sh './mvnw clean package'
                }
            }
        }
    }
}