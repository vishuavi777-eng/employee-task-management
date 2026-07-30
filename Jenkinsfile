pipeline {

    agent any

    tools {
        jdk 'JDK-17'
    }

    options {
        buildDiscarder(
            logRotator(numToKeepStr: '20')
        )

        timestamps()
    }

    parameters {

        choice(
            name: 'SERVICE_TO_BUILD',
            choices: [
                'ALL',
                'config-server',
                'eureka-server',
                'api-gateway',
                'employee-service',
                'task-service',
                'notification-service'
            ],
            description: 'Select the service to build'
        )

        choice(
            name: 'DEPLOY_ENVIRONMENT',
            choices: ['DEV', 'QA', 'PROD'],
            description: 'Select the target environment'
        )

        booleanParam(
            name: 'RUN_TESTS',
            defaultValue: true,
            description: 'Run unit tests'
        )
    }

    stages {

        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Build Environment') {
            steps {
                sh '''
                    echo "Jenkins user: $(whoami)"
                    echo "JAVA_HOME: $JAVA_HOME"
                    echo "PATH: $PATH"
                    java -version
                '''

                dir('employee-service') {
                    sh './mvnw -version'
                }
            }
        }

        stage('Build Config Server') {
            when {
                expression {
                    shouldBuild('config-server')
                }
            }

            steps {
                buildService('config-server')
            }
        }

        stage('Build Eureka Server') {
            when {
                expression {
                    shouldBuild('eureka-server')
                }
            }

            steps {
                buildService('eureka-server')
            }
        }

        stage('Build API Gateway') {
            when {
                expression {
                    shouldBuild('api-gateway')
                }
            }

            steps {
                buildService('api-gateway')
            }
        }

        stage('Build Employee Service') {
            when {
                expression {
                    shouldBuild('employee-service')
                }
            }

            steps {
                buildService('employee-service')
            }
        }

        stage('Build Task Service') {
            when {
                expression {
                    shouldBuild('task-service')
                }
            }

            steps {
                buildService('task-service')
            }
        }

        stage('Build Notification Service') {
            when {
                expression {
                    shouldBuild('notification-service')
                }
            }

            steps {
                buildService('notification-service')
            }
        }
    }

    post {

        always {

            junit(
                testResults: '**/target/surefire-reports/*.xml',
                allowEmptyResults: true
            )

            archiveArtifacts(
                artifacts: '**/target/*.jar',
                fingerprint: true,
                allowEmptyArchive: false
            )
        }

        success {
            echo 'Selected services built successfully.'
        }

        failure {
            echo 'Pipeline failed. Check the environment and failed stage.'
        }
    }
}

boolean shouldBuild(String serviceName) {
    return params.SERVICE_TO_BUILD == 'ALL' ||
           params.SERVICE_TO_BUILD == serviceName
}

void buildService(String serviceName) {

    dir(serviceName) {

        sh 'chmod +x mvnw'

        if (params.RUN_TESTS) {
            sh './mvnw clean package'
        } else {
            sh './mvnw clean package -DskipTests'
        }
    }
}