pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
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
                echo ===== STOPPING TOMCAT (FORCE) =====
                taskkill /F /IM java.exe /T || echo Tomcat already stopped

                timeout /t 5

                echo ===== CLEAN OLD DEPLOYMENT =====
                rmdir /S /Q "%CATALINA_HOME%\\webapps\\ReusableComponentDemo-0.0.1-SNAPSHOT" 2>nul
                del /Q "%CATALINA_HOME%\\webapps\\ReusableComponentDemo-0.0.1-SNAPSHOT.war" 2>nul

                echo ===== COPYING NEW WAR =====
                copy /Y target\\ReusableComponentDemo-0.0.1-SNAPSHOT.war "%CATALINA_HOME%\\webapps\\"

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
