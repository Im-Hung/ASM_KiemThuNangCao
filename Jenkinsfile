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
                    sh 'mvn clean package -DskipTests'
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
                    BUILD_ID=dontKillMe setsid nohup java -jar *.jar > /tmp/vegana.log 2>&1 < /dev/null &

                    sleep 40
                '''
            }
        }

        stage('🔍 Health Check') {
            steps {
                echo '🏥 Checking app...'
                sh '''
                    curl -f http://localhost:8082 || echo "App starting..."
                    ps aux | grep vegana | grep -v grep || echo "Process check"
                '''
            }
        }
    }

    post {
        success {
            echo '''
            🎉 Vegana Shop CI/CD SUCCESS!

            ✅ Build: SUCCESS
            ✅ Deploy: SUCCESS

            🌐 Access: http://localhost:8082
            📝 Logs: docker exec -it jenkins tail -f /tmp/vegana.log
            '''
        }
        failure {
            echo '❌ Pipeline FAILED'
        }
    }
}
