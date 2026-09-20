def call() {

    pipeline {

        agent any

        stages {

            stage('Clone') {
                steps {
                    echo "Cloning Redis Ansible repository..."

                    git branch: 'main',
                        url: 'https://github.com/Megha-Malik/role-ansible.git'
                }
            }

            stage('User Approval') {
                steps {
                    input message: 'Do you want to deploy Redis?',
                          ok: 'Proceed'
                }
            }

            stage('Playbook Execution') {
                steps {
                    sh '''
                        ansible-playbook -i inventory playbook.yml
                    '''
                }
            }
        }

        post {

            success {
                slackSend(
                    channel: '#all-megha',
                    message: "SUCCESS: Redis deployment completed. Job: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    color: 'good'
                )
            }

            failure {
                slackSend(
                    channel: '#all-megha',
                    message: "FAILED: Redis deployment failed. Job: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    color: 'danger'
                )
            }
        }
    }
}


