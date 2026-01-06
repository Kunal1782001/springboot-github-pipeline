pipeline {
    agent any

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        DOCKER_IMAGE = 'springboot-app'
        MYSQL_IMAGE  = 'mysql:8.0'

        APP_CONTAINER   = 'springboot-container'
        MYSQL_CONTAINER = 'mysql-db'

        MYSQL_ROOT_PASSWORD = 'root'
        MYSQL_DATABASE      = 'demo_reusable_component'

        APP_PORT = '8080'
        MYSQL_HOST_PORT = '3307'
        MYSQL_CONTAINER_PORT = '3306'

        DOCKER_NETWORK = 'spring-mysql-network'
    }

    stages {

        stage('Clean Workspace') {
            steps {
                echo '🧹 Cleaning workspace...'
                cleanWs()
            }
        }

        stage('Checkout') {
            steps {
                echo '📥 Checking out code...'
                git branch: 'main',
                    url: 'https://github.com/Kunal1782001/springboot-github-pipeline.git'
            }
        }

        stage('Build JAR') {
            steps {
                echo '🔨 Building Spring Boot JAR...'
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '🐳 Building Docker image...'
                bat "docker build -t %DOCKER_IMAGE%:%BUILD_NUMBER% ."
                bat "docker tag %DOCKER_IMAGE%:%BUILD_NUMBER% %DOCKER_IMAGE%:latest"
            }
        }

        stage('Stop Old Containers') {
            steps {
                echo '🛑 Stopping old containers...'
                bat '''
                docker stop %APP_CONTAINER% 2>nul
                docker rm %APP_CONTAINER% 2>nul
                docker stop %MYSQL_CONTAINER% 2>nul
                docker rm %MYSQL_CONTAINER% 2>nul
                '''
            }
        }

        stage('Create Docker Network') {
            steps {
                echo '🌐 Creating Docker network...'
                bat '''
                docker network rm %DOCKER_NETWORK% 2>nul
                docker network create %DOCKER_NETWORK%
                '''
            }
        }

        stage('Deploy MySQL') {
            steps {
                echo '🗄️ Starting MySQL container...'
                bat """
                docker run -d ^
                  --name %MYSQL_CONTAINER% ^
                  --network %DOCKER_NETWORK% ^
                  -e MYSQL_ROOT_PASSWORD=%MYSQL_ROOT_PASSWORD% ^
                  -e MYSQL_DATABASE=%MYSQL_DATABASE% ^
                  -p %MYSQL_HOST_PORT%:%MYSQL_CONTAINER_PORT% ^
                  %MYSQL_IMAGE%
                """
            }
        }

        stage('Wait for MySQL') {
            steps {
                echo '⏳ Waiting for MySQL to be ready...'
                bat '''
                timeout /t 30 /nobreak
                docker exec %MYSQL_CONTAINER% mysqladmin ping -h localhost -u root -p%MYSQL_ROOT_PASSWORD%
                '''
            }
        }

        stage('Deploy Spring Boot App') {
            steps {
                echo '🚀 Starting Spring Boot container...'
                bat """
                docker run -d ^
                  --name %APP_CONTAINER% ^
                  --network %DOCKER_NETWORK% ^
                  -p %APP_PORT%:%APP_PORT% ^
                  -e SPRING_DATASOURCE_URL=jdbc:mysql://%MYSQL_CONTAINER%:%MYSQL_CONTAINER_PORT%/%MYSQL_DATABASE%?useSSL=false^&allowPublicKeyRetrieval=true ^
                  -e SPRING_DATASOURCE_USERNAME=root ^
                  -e SPRING_DATASOURCE_PASSWORD=%MYSQL_ROOT_PASSWORD% ^
                  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update ^
                  %DOCKER_IMAGE%:latest
                """
            }
        }

        stage('Wait for Application') {
            steps {
                echo '⏳ Waiting for application startup...'
                bat 'timeout /t 30 /nobreak'
            }
        }

        stage('Show Status') {
            steps {
                echo '📊 Container status'
                bat 'docker ps'
            }
        }
    }

    post {
        success {
            echo '✅ DEPLOYMENT SUCCESSFUL!'
            echo "🌐 App URL: http://localhost:${APP_PORT}"
            echo "🗄️ MySQL: localhost:${MYSQL_HOST_PORT}"
        }

        failure {
            echo '❌ DEPLOYMENT FAILED'
            bat '''
            docker ps -a
            docker logs %MYSQL_CONTAINER% 2>nul
            docker logs %APP_CONTAINER% 2>nul
            '''
        }

        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
        }
    }
}
