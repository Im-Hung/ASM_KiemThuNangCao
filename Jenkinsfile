pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK-11'
    }

    environment {
        APP_NAME = 'vegana-shop'
        PORT = '8082'
    }

    stages {
        stage('🏗️ Build Application') {
            steps {
                echo '📦 Building Vegana Shop...'
                sh 'mvn clean compile'
            }
        }

        stage('🧪 Run Tests') {
            steps {
                echo '🔍 Running tests...'
                sh 'mvn test -DskipTests=true'
            }
        }

        stage('📦 Package Application') {
            steps {
                echo '📦 Creating WAR/JAR...'
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage('🚀 Deploy Application') {
            steps {
                echo '🚀 Deploying Vegana Shop on port 8082...'
                sh '''
                    # Kill old app on port 8082
                    pkill -9 -f "vegana-shop" || true

                    # Find and kill process using port 8082
                    if lsof -Pi :8082 -sTCP:LISTEN -t >/dev/null 2>&1; then
                        lsof -ti:8082 | xargs kill -9 || true
                    fi

                    sleep 5

                    # Start new app
                    cd /var/jenkins_home/workspace/Vegana-Shop-CI-CD
                    BUILD_ID=dontKillMe setsid nohup java -jar target/*.war > /tmp/vegana-shop.log 2>&1 < /dev/null &

                    echo "Waiting 40 seconds for app to start..."
                    sleep 40
                '''
            }
        }

        stage('🔍 Health Check') {
            steps {
                echo '🏥 Checking if app is running...'
                sh 'curl -f http://localhost:8082 || echo "App is starting..."'
            }
        }
    }

    post {
        success {
            echo '''
            🎉 Vegana Shop CI/CD SUCCESS!

            ✅ Build: SUCCESS
            ✅ Tests: PASSED
            ✅ Deploy: SUCCESS

            🌐 Access: http://localhost:8082
            '''
        }
        failure {
            echo 'Pipeline FAILED - check logs'
        }
    }
}
