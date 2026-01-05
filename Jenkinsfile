pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        TOMCAT_HOME = 'C:\\apache-tomcat-10.1.18'
        WAR_NAME = 'springboot-github-pipeline.war'
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
                echo Stopping Tomcat...
                "%TOMCAT_HOME%\\bin\\shutdown.bat"

                timeout /t 10

                echo Copying WAR to Tomcat webapps...
                copy /Y target\\*.war "%TOMCAT_HOME%\\webapps\\%WAR_NAME%"

                echo Starting Tomcat...
                "%TOMCAT_HOME%\\bin\\startup.bat"
                """
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.war', allowEmptyArchive: false
        }
    }
}
