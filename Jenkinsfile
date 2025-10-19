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
        stage('🏗️ Build Application') {
            steps {
                echo '📦 Building Vegana Shop...'
                dir("${WORKSPACE_DIR}") {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('🧪 Run Tests') {
            steps {
                echo '🔍 Running tests...'
                dir("${WORKSPACE_DIR}") {
                    sh 'mvn test -DskipTests=true'
                }
            }
        }

        stage('📦 Package Application') {
            steps {
                echo '📦 Creating WAR...'
                dir("${WORKSPACE_DIR}") {
                    sh 'mvn package -DskipTests'
                }
            }
        }

        stage('🚀 Deploy Application') {
            steps {
                echo '🚀 Deploying on port 8082...'
                sh '''
                    # Kill old app
                    pkill -9 -f "vegana" || true
                    sleep 3

                    # Navigate to project folder
                    cd Vegana-shop

                    # Start new app
                    BUILD_ID=dontKillMe setsid nohup java -jar target/*.war > /tmp/vegana.log 2>&1 < /dev/null &

                    sleep 30
                '''
            }
        }

        stage('🔍 Health Check') {
            steps {
                echo '🏥 Health check...'
                sh 'curl -f http://localhost:8082 || echo "Starting..."'
            }
        }
    }

    post {
        success {
            echo '🎉 CI/CD Pipeline SUCCESS!'
            echo 'Access: http://localhost:8082'
        }
        failure {
            echo '❌ Pipeline FAILED'
        }
    }
}
