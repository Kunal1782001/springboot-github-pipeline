pipeline {
    agent any

    options {
        timeout(time: 20, unit: 'MINUTES')
    }

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    stages {

        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Kunal1782001/springboot-github-pipeline.git'
            }
        }

        stage('Build Info') {
            steps {
                echo "Job Name     : ${JOB_NAME}"
                echo "Build Number : ${BUILD_NUMBER}"
                echo "Git Commit   : ${GIT_COMMIT}"
            }
        }

        stage('Build JAR') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t springboot-app .'
            }
        }

        stage('Docker Deploy') {
            steps {
                bat 'docker stop springboot-container || echo Container not running'
                bat 'docker rm springboot-container || echo Container removed'
                bat 'docker run -d -p 8080:8080 --name springboot-container springboot-app'
            }
        }
    }

    post {
        success {
            echo "✅ Application deployed successfully in Docker"
        }
        failure {
            echo "❌ Deployment failed – check logs"
        }
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }
}
