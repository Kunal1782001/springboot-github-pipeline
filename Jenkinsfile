pipeline {
    agent any

    tools {
        maven 'Maven'   // Must match the name configured in Jenkins Global Tool Configuration
        jdk 'JDK17'     // Must match JDK configured in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Use your actual GitHub repo URL here
                
                git branch: 'main',
                    url: 'https://github.com/Kunal1782001/springboot-github-pipeline.git'
            }
        }

        stage('Build') {
            steps {
                // Windows: use bat, not sh
                bat 'mvn clean package'
            }
        }
    }
}




