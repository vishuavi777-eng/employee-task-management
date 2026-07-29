pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Package') {
            steps {
                dir('employee-service') {
                    sh 'mvn clean package'
                }
            }
        }

    }
}