pipeline {
    agent any

    environment {
        IMAGE_NAME = 'calculator-api'
        IMAGE_TAG  = 'v1'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    sh '''
                        echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USER" --password-stdin
                        docker tag ${IMAGE_NAME}:${IMAGE_TAG} abumaniyar/${IMAGE_NAME}:${IMAGE_TAG}
                        docker push abumaniyar/${IMAGE_NAME}:${IMAGE_TAG}
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker compose -p calculator down --remove-orphans || true
                    docker compose -p calculator pull
                    docker compose -p calculator up -d --remove-orphans
                '''

                sh '''
                    echo "Waiting for the API to become healthy..."
                    for i in $(seq 1 30); do
                        if curl -fsS http://localhost:8083/actuator/health; then
                            echo "Deployment health check passed!"
                            exit 0
                        fi
                        sleep 5
                    done

                    echo "API did not become healthy in time."
                    docker compose -p calculator ps
                    docker compose -p calculator logs --tail=100
                    exit 1
                '''
            }
        }
    }

    post {
        success {
            emailext(
                subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    Build completed successfully.

                    Job: ${env.JOB_NAME}
                    Build: ${env.BUILD_NUMBER}
                    URL: ${env.BUILD_URL}
                """,
                to: "Abutalha.Maniyar@iiitb.ac.in"
            )
        }

        failure {
            emailext(
                subject: "FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    Build failed.

                    Job: ${env.JOB_NAME}
                    Build: ${env.BUILD_NUMBER}
                    Console: ${env.BUILD_URL}console
                """,
                to: "Abutalha.Maniyar@iiitb.ac.in"
            )
        }
    }
} 