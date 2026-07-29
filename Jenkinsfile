pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('employee-service') {
                    sh './mvnw clean test'
                }
            }
        }

        stage('Package') {
            steps {
                dir('employee-service') {
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
    }
}