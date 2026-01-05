pipeline {
    agent any

    tools {
        maven 'Maven'   // Must match the name configured in Jenkins Global Tool Configuration
        jdk 'JDK17'     // Must match JDK configured in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Kunal1782001/springboot-github-pipeline.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }
    }

    post {
        always {
            // Archive the generated JAR so it can be downloaded from Jenkins
            archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: false
        }
    }
}
