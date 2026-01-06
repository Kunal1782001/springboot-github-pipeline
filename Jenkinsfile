pipeline {
    agent any

    tools {
        maven 'Maven'   
        jdk 'JDK17'
    }

    environment {
        CATALINA_HOME = 'C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1'
        APP_WAR = 'ReusableComponentDemo-0.0.1-SNAPSHOT.war'
        TOMCAT_SERVICE = 'Tomcat10'
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
                echo ===============================
                echo STOPPING TOMCAT SERVICE
                echo ===============================
                net stop %TOMCAT_SERVICE% || echo Tomcat already stopped

                timeout /t 5

                echo ===============================
                echo CLEAN OLD DEPLOYMENT
                echo ===============================
                rmdir /S /Q "%CATALINA_HOME%\\webapps\\ReusableComponentDemo-0.0.1-SNAPSHOT" 2>nul
                del /Q "%CATALINA_HOME%\\webapps\\%APP_WAR%" 2>nul

                echo ===============================
                echo COPY NEW WAR
                echo ===============================
                copy /Y target\\%APP_WAR% "%CATALINA_HOME%\\webapps\\"

                echo ===============================
                echo STARTING TOMCAT SERVICE
                echo ===============================
                net start %TOMCAT_SERVICE%
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
