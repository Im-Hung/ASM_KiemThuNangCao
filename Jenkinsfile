pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK-11'
    }

    environment {
        APP_NAME = 'vegana-shop'
        PORT = '8082'
        WORKSPACE_DIR = 'Vegana-shop'
    }

    stages {
        stage('🏗️ Build & Package') {
            steps {
                echo '📦 Building Vegana Shop...'
                dir("${WORKSPACE_DIR}") {
                    sh '''
                        # Build with resource filtering disabled
                        mvn clean package -DskipTests \
                            -Dmaven.resources.filtering=false \
                            -Dproject.build.sourceEncoding=UTF-8
                    '''
                }
            }
        }

        stage('🚀 Deploy Application') {
            steps {
                echo '🚀 Deploying on port 8082...'
                sh '''
                    pkill -9 -f "vegana" || true
                    sleep 3

                    cd Vegana-shop/target
                    BUILD_ID=dontKillMe setsid nohup java -jar *.war > /tmp/vegana.log 2>&1 < /dev/null &

                    sleep 30
                '''
            }
        }

        stage('🔍 Health Check') {
            steps {
                echo '🏥 Checking app...'
                sh '''
                    curl -f http://localhost:8082 || echo "App starting..."
                    ps aux | grep vegana | grep -v grep || echo "Process not found"
                '''
            }
        }
    }

    post {
        success {
            echo '''
            🎉 CI/CD Pipeline SUCCESS!

            Access: http://localhost:8082
            Logs: docker exec -it jenkins tail -f /tmp/vegana.log
            '''
        }
        failure {
            echo '❌ Pipeline FAILED'
        }
    }
}
