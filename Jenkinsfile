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
                echo '🛑 Stopping old containers'
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
                echo '⏳ Waiting for MySQL to be ready...'
                script {
                    def mysqlReady = false
                    def attempts = 0
                    def maxAttempts = 60
                    
                    while (!mysqlReady && attempts < maxAttempts) {
                        try {
                            bat(script: 'docker exec mysql-db mysqladmin ping -h localhost -u root -proot --silent', returnStatus: true)
                            mysqlReady = true
                            echo '✅ MySQL is ready!'
                        } catch (Exception e) {
                            attempts++
                            echo "Attempt ${attempts}/${maxAttempts}: MySQL not ready yet, waiting..."
                            sleep(time: 2, unit: 'SECONDS')
                        }
                    }
                    
                    if (!mysqlReady) {
                        error('MySQL failed to start after 120 seconds')
                    }
                }
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
                echo '⏳ Waiting for Spring Boot application (60 seconds)...'
                sleep(time: 60, unit: 'SECONDS')
                
                script {
                    // Check if container is still running
                    def containerStatus = bat(
                        script: 'docker inspect -f {{.State.Running}} springboot-container',
                        returnStdout: true
                    ).trim()
                    
                    if (containerStatus == 'false') {
                        echo '❌ Container crashed! Showing logs...'
                        bat 'docker logs springboot-container'
                        error('Spring Boot application failed to start')
                    } else {
                        echo '✅ Application container is running!'
                    }
                }
            }
        }

        stage('Show Status') {
            steps {
                echo '📊 Deployment Status'
                bat '''
                echo === DOCKER CONTAINERS ===
                docker ps
                
                echo.
                echo === APPLICATION LOGS (last 30 lines) ===
                docker logs springboot-container --tail 30
                '''
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
            echo '❌ DEPLOYMENT FAILED - Showing detailed logs...'
            bat '''
            echo === CONTAINER STATUS ===
            docker ps -a
            
            echo.
            echo === MYSQL LOGS (last 50 lines) ===
            docker logs mysql-db --tail 50 2>&1 || echo "MySQL logs unavailable"
            
            echo.
            echo === SPRINGBOOT LOGS (all) ===
            docker logs springboot-container 2>&1 || echo "Spring Boot logs unavailable"
            
            exit /b 0
            '''
        }

        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
        }
    }
}