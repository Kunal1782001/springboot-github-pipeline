pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Kunal1782001/springboot-github-pipeline.git'
            }
        }

        stage('Build') {
            steps {
                // Use bat for Windows
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Archive JAR') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
}
