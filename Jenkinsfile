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
                echo '🧹 Cleaning workspace'
                cleanWs()
            }
        }

        stage('Checkout') {
            steps {
                echo '📥 Checking out code'
                git branch: 'main',
                    url: 'https://github.com/Kunal1782001/springboot-github-pipeline.git'
            }
        }

        stage('Build JAR') {
            steps {
                echo '🔨 Building JAR'
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '🐳 Building Docker image'
                bat "docker build -t %DOCKER_IMAGE%:%BUILD_NUMBER% ."
                bat "docker tag %DOCKER_IMAGE%:%BUILD_NUMBER% %DOCKER_IMAGE%:latest"
            }
        }

        stage('Stop Old Containers') {
            steps {
                echo '🛑 Stopping old containers (bulletproof)'
                bat '''
                docker stop springboot-container >nul 2>&1
                docker rm springboot-container >nul 2>&1

                docker stop mysql-db >nul 2>&1
                docker rm mysql-db >nul 2>&1

                exit /b 0
                '''
            }
        }

        stage('Create Docker Network') {
            steps {
                echo '🌐 Creating Docker network'
                bat '''
                docker network rm spring-mysql-network >nul 2>&1
                docker network create spring-mysql-network
                exit /b 0
                '''
            }
        }

        stage('Deploy MySQL') {
            steps {
                echo '🗄️ Starting MySQL'
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
                echo '⏳ Waiting for MySQL'
                bat '''
                timeout /t 30 /nobreak
                docker exec mysql-db mysqladmin ping -h localhost -u root -proot
                exit /b 0
                '''
            }
        }

        stage('Deploy Spring Boot App') {
            steps {
                echo '🚀 Starting Spring Boot app'
                bat """
                docker run -d ^
                  --name %APP_CONTAINER% ^
                  --network %DOCKER_NETWORK% ^
                  -p %APP_PORT%:%APP_PORT% ^
                  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/%MYSQL_DATABASE%?useSSL=false^&allowPublicKeyRetrieval=true ^
                  -e SPRING_DATASOURCE_USERNAME=root ^
                  -e SPRING_DATASOURCE_PASSWORD=%MYSQL_ROOT_PASSWORD% ^
                  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update ^
                  %DOCKER_IMAGE%:latest
                """
            }
        }

        stage('Wait for Application') {
            steps {
                echo '⏳ Waiting for application startup'
                bat 'timeout /t 30 /nobreak'
            }
        }

        stage('Show Status') {
            steps {
                echo '📊 Docker status'
                bat 'docker ps'
            }
        }
    }

    post {
        success {
            echo '✅ DEPLOYMENT SUCCESSFUL'
            echo "🌐 App URL : http://localhost:${APP_PORT}"
            echo "🗄️ MySQL   : localhost:${MYSQL_HOST_PORT}"
        }

        failure {
            echo '❌ DEPLOYMENT FAILED'
            bat '''
            docker ps -a
            docker logs mysql-db >nul 2>&1
            docker logs springboot-container >nul 2>&1
            exit /b 0
            '''
        }

        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
        }
    }
}
