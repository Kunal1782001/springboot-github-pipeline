pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
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

        stage('Create ZIP') {
            steps {
                bat '''
                if exist build.zip del build.zip
                powershell Compress-Archive -Path target\\* -DestinationPath build.zip
                '''
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'build.zip', allowEmptyArchive: false
        }
    }
}
