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

<<<<<<< HEAD
        stage('Build Info') {
            steps {
                echo "Job Name     : ${JOB_NAME}"
                echo "Build Number : ${BUILD_NUMBER}"
                echo "Git Commit   : ${GIT_COMMIT}"
            }
        }

=======
>>>>>>> c7d5e00b60fa7456264a55ac88c14d01b524c21b
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
<<<<<<< HEAD
                bat 'docker stop springboot-container || echo Container not running'
                bat 'docker rm springboot-container || echo Container removed'
=======
                bat script: 'docker stop springboot-container', returnStatus: true
                bat script: 'docker rm springboot-container', returnStatus: true

>>>>>>> c7d5e00b60fa7456264a55ac88c14d01b524c21b
                bat 'docker run -d -p 8080:8080 --name springboot-container springboot-app'
            }
        }
    }

    post {
        success {
<<<<<<< HEAD
            echo "✅ Application deployed successfully in Docker"
=======
            echo "🚀 Application deployed successfully in Docker"
>>>>>>> c7d5e00b60fa7456264a55ac88c14d01b524c21b
        }
        failure {
            echo "❌ Deployment failed – check logs"
        }
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }
}
