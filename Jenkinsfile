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
        MYSQL_IMAGE = 'mysql:8.0'
        APP_CONTAINER = 'springboot-container'
        MYSQL_CONTAINER = 'mysql-db'
        MYSQL_ROOT_PASSWORD = 'root'
        MYSQL_DATABASE = 'demo_reusable_component'
        APP_PORT = '8080'
        MYSQL_HOST_PORT = '3307'  // CHANGED: Use 3307 instead of 3306 to avoid conflict
        MYSQL_CONTAINER_PORT = '3306'
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
                echo '🔨 Building JAR with Maven (skipping tests)...'
                bat 'mvn clean package -DskipTests'
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
        echo '🛑 Stopping old containers...'
        script {
            bat '''
            @echo off
            echo Cleaning up old containers...

            docker ps -a --format "{{.Names}}" | findstr /R "^springboot-container$" >nul
            IF %ERRORLEVEL% EQU 0 (
                docker stop springboot-container
                docker rm springboot-container
            )

            docker ps -a --format "{{.Names}}" | findstr /R "^mysql-db$" >nul
            IF %ERRORLEVEL% EQU 0 (
                docker stop mysql-db
                docker rm mysql-db
            )

            echo Cleanup complete!
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
                    docker network rm spring-mysql-network 2>nul || echo Network removed or did not exist
                    docker network create spring-mysql-network
                    '''
                }
            }
        }

        stage('Deploy MySQL') {
            steps {
                echo '🗄️ Deploying MySQL container on port 3307...'
                bat """
                docker run -d ^
                    --name %MYSQL_CONTAINER% ^
                    --network spring-mysql-network ^
                    -e MYSQL_ROOT_PASSWORD=%MYSQL_ROOT_PASSWORD% ^
                    -e MYSQL_DATABASE=%MYSQL_DATABASE% ^
                    -p %MYSQL_HOST_PORT%:%MYSQL_CONTAINER_PORT% ^
                    %MYSQL_IMAGE%
                """
                echo "✅ MySQL will be accessible on host port ${MYSQL_HOST_PORT}"
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
                    set MAX_RETRIES=12
                    set RETRY_COUNT=0
                    
                    :LOOP
                    docker exec %MYSQL_CONTAINER% mysqladmin ping -h localhost -u root -p%MYSQL_ROOT_PASSWORD% >nul 2>&1
                    IF ERRORLEVEL 1 (
                        set /a RETRY_COUNT+=1
                        if %RETRY_COUNT% GEQ %MAX_RETRIES% (
                            echo MySQL failed to start
                            docker logs %MYSQL_CONTAINER%
                            exit /b 1
                        )
                        echo Retrying... (%RETRY_COUNT%/%MAX_RETRIES%)
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
                echo '⏳ Waiting for Spring Boot to start...'
                bat 'timeout /t 30 /nobreak'
            }
        }

        stage('Display Container Status') {
            steps {
                echo '📊 Container Status:'
                bat 'docker ps -a --filter "name=%APP_CONTAINER%" --filter "name=%MYSQL_CONTAINER%"'
            }
        }

        stage('Display Application Logs') {
            steps {
                echo '📜 Application Logs:'
                bat 'docker logs --tail 100 %APP_CONTAINER%'
            }
        }

    }

    post {
        success {
            echo '✅ ═══════════════════════════════════════'
            echo '✅ Deployment Successful!'
            echo '✅ ═══════════════════════════════════════'
            echo "✅ Application: http://localhost:${APP_PORT}"
            echo "✅ MySQL (Docker): localhost:${MYSQL_HOST_PORT}"
            echo "✅ MySQL (Internal): ${MYSQL_CONTAINER}:${MYSQL_CONTAINER_PORT}"
            echo '✅ ═══════════════════════════════════════'
        }
        
        failure {
            echo '❌ Deployment Failed - See diagnostics below'
            script {
                bat '''
                @echo off
                echo === Containers ===
                docker ps -a
                echo === Networks ===
                docker network ls
                echo === Port Usage ===
                netstat -ano | findstr :3307
                netstat -ano | findstr :8080
                echo === App Logs ===
                docker logs %APP_CONTAINER% 2>nul || echo No app container
                echo === MySQL Logs ===
                docker logs %MYSQL_CONTAINER% 2>nul || echo No MySQL container
                '''
            }
        }
        
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
            script {
                bat 'FOR /F "tokens=*" %%i IN (\'docker images -f "dangling=true" -q 2^>nul\') DO docker rmi %%i 2>nul || exit /b 0'
            }
        }
    }
}
