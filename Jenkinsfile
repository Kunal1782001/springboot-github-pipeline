pipeline {
    agent any

    tools {
        maven 'Maven'     // Jenkins → Global Tool Configuration
        jdk 'JDK17'       // Jenkins → Global Tool Configuration
    }

    environment {
        CATALINA_HOME = 'C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Kunal1782001/springboot-github-pipeline.git'
            }
        }

        stage('Build WAR') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                bat """
                echo ===== STOPPING TOMCAT =====
                "%CATALINA_HOME%\\bin\\shutdown.bat"

                timeout /t 10

                echo ===== DEPLOYING WAR =====
                copy /Y target\\*.war "%CATALINA_HOME%\\webapps\\"

                echo ===== STARTING TOMCAT =====
                "%CATALINA_HOME%\\bin\\startup.bat"
                """
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.war', fingerprint: true
        }
    }
}
