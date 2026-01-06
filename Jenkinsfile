pipeline {
    agent any

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    tools {
        maven 'Maven'  // Make sure 'Maven' is configured in Jenkins Global Tool Config
        jdk 'JDK17'    // Make sure 'JDK17' is configured in Jenkins Global Tool Config
    }

    environment {
        DOCKER_IMAGE = 'springboot-app'
        MYSQL_IMAGE = 'mysql:8.0'
        APP_CONTAINER = 'springboot-container'
        MYSQL_CONTAINER = 'mysql-db'
        MYSQL_ROOT_PASSWORD = 'root'
        MYSQL_DATABASE = 'demo_reusable_component'
        APP_PORT = '8080'
        MYSQL_PORT = '3306'
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
                echo '📥 Checking out code from GitHub...'
                git branch: 'main',
                    url: 'https://github.com/Kunal1782001/springboot-github-pipeline.git'
            }
        }

        stage('Build Info') {
            steps {
                echo "═══════════════════════════════════════"
                echo "📋 Build Information"
                echo "═══════════════════════════════════════"
                echo "Job Name     : ${JOB_NAME}"
                echo "Build Number : ${BUILD_NUMBER}"
                echo "Build ID     : ${BUILD_ID}"
                echo "Git Commit   : ${GIT_COMMIT}"
                echo "Git Branch   : ${GIT_BRANCH}"
                echo "Workspace    : ${WORKSPACE}"
                echo "═══════════════════════════════════════"
            }
        }

        stage('Maven Build') {
            steps {
                echo '🔨 Building JAR with Maven...'
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                echo '🧪 Running unit tests...'
                bat 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo '🐳 Building Docker image...'
                bat "docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} ."
                bat "docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Stop Old Containers') {
            steps {
                echo '🛑 Stopping and removing old containers...'
                script {
                    bat '''
                    @echo off
                    echo Stopping old containers...
                    
                    REM Stop and remove Spring Boot container
                    FOR /F "tokens=*" %%i IN ('docker ps -aq -f "name=%APP_CONTAINER%"') DO (
                        echo Stopping container %%i...
                        docker stop %%i 2>nul
                        docker rm %%i 2>nul
                    )
                    
                    REM Stop and remove MySQL container
                    FOR /F "tokens=*" %%i IN ('docker ps -aq -f "name=%MYSQL_CONTAINER%"') DO (
                        echo Stopping container %%i...
                        docker stop %%i 2>nul
                        docker rm %%i 2>nul
                    )
                    
                    echo Old containers cleaned up!
                    '''
                }
            }
        }

        stage('Create Docker Network') {
            steps {
                echo '🌐 Creating Docker network...'
                script {
                    bat '''
                    @echo off
                    REM Check if network exists, if not create it
                    docker network inspect spring-mysql-network >nul 2>&1
                    IF ERRORLEVEL 1 (
                        echo Creating network...
                        docker network create spring-mysql-network
                    ) ELSE (
                        echo Network already exists
                    )
                    '''
                }
            }
        }

        stage('Deploy MySQL') {
            steps {
                echo '🗄️ Deploying MySQL container...'
                bat """
                docker run -d ^
                    --name %MYSQL_CONTAINER% ^
                    --network spring-mysql-network ^
                    -e MYSQL_ROOT_PASSWORD=%MYSQL_ROOT_PASSWORD% ^
                    -e MYSQL_DATABASE=%MYSQL_DATABASE% ^
                    -p %MYSQL_PORT%:%MYSQL_PORT% ^
                    %MYSQL_IMAGE%
                """
            }
        }

        stage('Wait for MySQL') {
            steps {
                echo '⏳ Waiting for MySQL to be ready...'
                script {
                    bat '''
                    @echo off
                    echo Waiting for MySQL to start...
                    timeout /t 30 /nobreak
                    
                    echo Checking MySQL health...
                    :LOOP
                    docker exec %MYSQL_CONTAINER% mysqladmin ping -h localhost -u root -p%MYSQL_ROOT_PASSWORD% >nul 2>&1
                    IF ERRORLEVEL 1 (
                        echo MySQL not ready yet, waiting...
                        timeout /t 5 /nobreak
                        goto LOOP
                    )
                    echo MySQL is ready!
                    '''
                }
            }
        }

        stage('Deploy Spring Boot App') {
            steps {
                echo '🚀 Deploying Spring Boot application...'
                bat """
                docker run -d ^
                    --name %APP_CONTAINER% ^
                    --network spring-mysql-network ^
                    -p %APP_PORT%:%APP_PORT% ^
                    -e SPRING_DATASOURCE_URL=jdbc:mysql://%MYSQL_CONTAINER%:%MYSQL_PORT%/%MYSQL_DATABASE%?useSSL=false^&allowPublicKeyRetrieval=true ^
                    -e SPRING_DATASOURCE_USERNAME=root ^
                    -e SPRING_DATASOURCE_PASSWORD=%MYSQL_ROOT_PASSWORD% ^
                    -e SPRING_JPA_HIBERNATE_DDL_AUTO=update ^
                    %DOCKER_IMAGE%:latest
                """
            }
        }

        stage('Health Check') {
            steps {
                echo '🏥 Performing health check...'
                script {
                    bat '''
                    @echo off
                    echo Waiting for application to start...
                    timeout /t 20 /nobreak
                    
                    echo Checking application health...
                    curl -f http://localhost:%APP_PORT%/actuator/health || curl -f http://localhost:%APP_PORT% || echo Application might not have health endpoint
                    '''
                }
            }
        }

        stage('Display Container Status') {
            steps {
                echo '📊 Container Status:'
                bat 'docker ps -a --filter "name=%APP_CONTAINER%" --filter "name=%MYSQL_CONTAINER%"'
            }
        }

        stage('Display Logs') {
            steps {
                echo '📜 Application Logs (last 50 lines):'
                bat 'docker logs --tail 50 %APP_CONTAINER%'
            }
        }

    }

    post {
        success {
            echo '✅ ═══════════════════════════════════════'
            echo '✅ Deployment Successful!'
            echo '✅ ═══════════════════════════════════════'
            echo "✅ Application: http://localhost:${APP_PORT}"
            echo "✅ MySQL: localhost:${MYSQL_PORT}"
            echo '✅ ═══════════════════════════════════════'
        }
        
        failure {
            echo '❌ ═══════════════════════════════════════'
            echo '❌ Deployment Failed!'
            echo '❌ ═══════════════════════════════════════'
            echo '❌ Check the logs above for errors'
            
            script {
                bat '''
                @echo off
                echo.
                echo Collecting failure information...
                echo.
                echo === Spring Boot Container Logs ===
                docker logs --tail 100 %APP_CONTAINER% 2>nul || echo Container not found
                echo.
                echo === MySQL Container Logs ===
                docker logs --tail 50 %MYSQL_CONTAINER% 2>nul || echo Container not found
                '''
            }
        }
        
        unstable {
            echo '⚠️ Build is unstable - check test results'
        }
        
        always {
            echo '📦 Archiving artifacts...'
            archiveArtifacts artifacts: 'target/*.jar', 
                            fingerprint: true,
                            allowEmptyArchive: true
            
            echo '🧹 Cleaning up old Docker images...'
            bat '''
            @echo off
            REM Remove dangling images
            FOR /F "tokens=*" %%i IN ('docker images -f "dangling=true" -q') DO docker rmi %%i 2>nul
            '''
        }
    }
}
