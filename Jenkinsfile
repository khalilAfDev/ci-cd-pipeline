pipeline {

    agent any

    environment {
      REGISTRY = "772004002961.dkr.ecr.eu-west-1.amazonaws.com/grp_04/cicdecr"
      AWS_DEFAULT_REGION = "eu-west-1"
      AWS_ACCOUNT_ID = "772004002961"
      IMAGE_REPO_NAME = "grp_04/cicdecr"
    }
    
   tools {
        maven 'MAVEN_HOME'
    }
    
    stages {
        stage('Step 1 - Cloning the project') {
            steps {
                git branch: 'main', url: 'https://github.com/oadya/ci-cd-pipeline.git'
            }
        }
        
        stage('Step 2 - Build the package') {
            steps {
              sh 'mvn clean package'
            }
     
        }
        
        
        stage("Step 3 - Sonar Quality Check") {
            steps {
                script{
                  withSonarQubeEnv(installationName : 'sonar_server', credentialsId : 'jenkins-sonar-token-id') {
                    sh 'mvn sonar:sonar'
                  }
                timeout(time: 20, unit: 'SECONDS') {
                  def qg = waitForQualityGate()
                  if (qg.status != 'OK') {
                      error "Pipeline aborted due to quality gate failure: ${qg.status}"
                  }
                }
               }
            }
        }
        stage("Step 4 - Building Docker image") {
            steps {
                script {
                    sh """
                        pwd
                        ls -la
                        cat Dockerfile
                        docker build -t ${REGISTRY}:${BUILD_NUMBER} .
                    """
                }
            }
        }
        
    stage("Step 5 - Logging into AWS ECR") {
        steps {
            script {
                sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
            }
 
        }
    }    
    
        stage("Step 6 - Push image to AWS ECR") {
            steps {
                script{
                     sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:$BUILD_NUMBER"

                }
            }
        }        
         
        stage("Step 7 - Cleaning up images") {
            steps {
                script{
                     sh "docker rmi  ${AWS_ACCOUNT_ID}.dkr.ecr.${}.amazonaws.com/${IMAGE_REPO_NAME}:$BUILD_NUMBER"

                }
            }
        } 
    
        stage("Step 8 - Deploy via Ansible") {
            steps {
                  ansiblePlaybook credentialsId: 'df561976-e106-4f6c-9787-5988cec02618', disableHostKeyChecking: true, installation: 'Ansible', inventory: 'hosts.ini', playbook: 'playbook.yml', vaultTmpPath: ''
            }
        }
    }
    
    
}


