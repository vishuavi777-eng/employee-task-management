def services = [
    'config-server',
    'eureka-server',
    'api-gateway',
    'employee-service',
    'task-service',
    'notification-service'
]

pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Services') {
            steps {
                script {
                    for (service in services) {
                        dir(service) {
                            sh './mvnw clean package'
                        }
                    }
                }
            }
        }

//         stage('Build & Package Employee Service') {
//             steps {
//                 dir('employee-service') {
//                     sh './mvnw clean package'
//                 }
//             }
//         }
//
//         stage('Build & Package Task Service') {
//             steps {
//                 dir('task-service') {
//                     sh './mvnw clean package'
//                 }
//             }
//         }
    }

    post {
        always {
            echo 'Pipeline finished'
        }

        success {
            echo 'Build Successful'
        }

        failure {
            echo 'Build Failed'
        }
    }
}