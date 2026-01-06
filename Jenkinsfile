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
                echo "Job Name      : ${JOB_NAME}"
                echo "Build Number  : ${BUILD_NUMBER}"
                echo "Branch Name   : ${BRANCH_NAME}"
                echo "Git Commit    : ${GIT_COMMIT}"
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Build JAR') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                // Builds Docker image from Dockerfile
                bat 'docker build -t springboot-app .'
            }
        }

       stage('Docker Deploy') {
    steps {
        // Stop old container if running (ignore errors)
        bat script: 'docker stop springboot-container', returnStatus: true
        bat script: 'docker rm springboot-container', returnStatus: true

        // Run new container
        bat 'docker run -d -p 8080:8080 --name springboot-container springboot-app'
    }
}
    }

   post {
    success {
        echo " Application deployed successfully in Docker"
    }
    failure {
        echo " Deployment failed – check logs"
    }
    always {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }
}

}
