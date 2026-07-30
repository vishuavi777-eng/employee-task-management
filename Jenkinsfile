pipeline {

    agent any

    tools {
        jdk 'JDK-17'
    }

    stage('Verify Java') {
        steps {
            sh '''
                echo "JAVA_HOME=$JAVA_HOME"
                java -version
            '''
        }
    }

//     options {
//         buildDiscarder(
//             logRotator(numToKeepStr: '10')
//         )
//
//         timestamps()
//     }
//
//     parameters {
//
//         choice(
//             name: 'SERVICE_TO_BUILD',
//             choices: [
//                 'ALL',
//                 'config-server',
//                 'eureka-server',
//                 'api-gateway',
//                 'employee-service',
//                 'task-service',
//                 'notification-service'
//             ],
//             description: 'Select the service to build'
//         )
//
//         choice(
//             name: 'DEPLOY_ENVIRONMENT',
//             choices: [
//                 'DEV',
//                 'QA',
//                 'PROD'
//             ],
//             description: 'Select the target environment'
//         )
//
//         booleanParam(
//             name: 'RUN_TESTS',
//             defaultValue: true,
//             description: 'Run unit tests'
//         )
//     }
//
//     environment {
//         PROJECT_NAME = 'Employee Task Management'
//         JAVA_VERSION = '17'
//     }
//
//     stages {
//
//         stage('Clean Workspace') {
//             steps {
//                 deleteDir()
//             }
//         }
//
//         stage('Checkout') {
//
//             steps {
//                 checkout scm
//             }
//         }
//
//         stage('Display Build Configuration') {
//
//             steps {
//
//                 echo "Project: ${env.PROJECT_NAME}"
//                 echo "Build number: ${env.BUILD_NUMBER}"
//                 echo "Selected service: ${params.SERVICE_TO_BUILD}"
//                 echo "Environment: ${params.DEPLOY_ENVIRONMENT}"
//                 echo "Run tests: ${params.RUN_TESTS}"
//             }
//         }
//
//         stage('Build Config Server') {
//
//             when {
//                 expression {
//                     shouldBuild('config-server')
//                 }
//             }
//
//             steps {
//                 buildService('config-server')
//             }
//         }
//
//         stage('Build Eureka Server') {
//
//             when {
//                 expression {
//                     shouldBuild('eureka-server')
//                 }
//             }
//
//             steps {
//                 buildService('eureka-server')
//             }
//         }
//
//         stage('Build API Gateway') {
//
//             when {
//                 expression {
//                     shouldBuild('api-gateway')
//                 }
//             }
//
//             steps {
//                 buildService('api-gateway')
//             }
//         }
//
//         stage('Build Employee Service') {
//
//             when {
//                 expression {
//                     shouldBuild('employee-service')
//                 }
//             }
//
//             steps {
//                 buildService('employee-service')
//             }
//         }
//
//         stage('Build Task Service') {
//
//             when {
//                 expression {
//                     shouldBuild('task-service')
//                 }
//             }
//
//             steps {
//                 buildService('task-service')
//             }
//         }
//
//         stage('Build Notification Service') {
//
//             when {
//                 expression {
//                     shouldBuild('notification-service')
//                 }
//             }
//
//             steps {
//                 buildService('notification-service')
//             }
//         }
//
//         stage('Production Approval') {
//
//             when {
//                 expression {
//                     params.DEPLOY_ENVIRONMENT == 'PROD'
//                 }
//             }
//
//             steps {
//
//                 timeout(time: 10, unit: 'MINUTES') {
//
//                     input(
//                         message: "Approve production deployment for build ${env.BUILD_NUMBER}?",
//                         ok: 'Deploy to Production',
//                         submitter: 'admin'
//                     )
//                 }
//             }
//         }
//
//         stage('Deployment Simulation') {
//
//             steps {
//
//                 echo """
//                 Deployment simulation:
//                 Service     : ${params.SERVICE_TO_BUILD}
//                 Environment : ${params.DEPLOY_ENVIRONMENT}
//                 Build       : ${env.BUILD_NUMBER}
//                 """
//             }
//         }
//     }
//
//     post {
//
//         always {
//
//             script {
//
//                 if (params.RUN_TESTS) {
//                     junit(
//                         testResults: '**/target/surefire-reports/*.xml',
//                         allowEmptyResults: true
//                     )
//                 }
//             }
//
//             archiveArtifacts(
//                 artifacts: '**/target/*.jar',
//                 fingerprint: true,
//                 allowEmptyArchive: false
//             )
//
//             echo "Pipeline completed with status: ${currentBuild.currentResult}"
//         }
//
//         success {
//             echo 'All selected services built successfully.'
//         }
//
//         failure {
//             echo 'Pipeline failed. Check the failed stage and console output.'
//         }
//
//         aborted {
//             echo 'Pipeline was aborted or production approval timed out.'
//         }
//     }
}

boolean shouldBuild(String serviceName) {

    return params.SERVICE_TO_BUILD == 'ALL' ||
           params.SERVICE_TO_BUILD == serviceName
}

void buildService(String serviceName) {

    dir(serviceName) {

        if (params.RUN_TESTS) {
            sh './mvnw clean package'
        } else {
            sh './mvnw clean package -DskipTests'
        }
    }
}