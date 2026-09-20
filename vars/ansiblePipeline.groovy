
def call() {

    pipeline {

        agent any

        stages {

            stage('Clone') {
                steps {

                    echo "=========================================="
                    echo " Stage 1: Clone"
                    echo "=========================================="

                    deleteDir()

                    git(
                        branch: 'main',
                        url: 'https://github.com/Megha-Malik/ansible-redis.git'
                    )

                    echo "Redis Ansible repository cloned successfully."
                }
            }

            stage('Load Configuration') {
                steps {

                    echo "=========================================="
                    echo " Loading Configuration"
                    echo "=========================================="

                    script {

                        def configFile = 'env/prod/prod.conf'

                        if (!fileExists(configFile)) {
                            error "Configuration file not found: ${configFile}"
                        }

                        def config = readProperties file: configFile

                        env.SLACK_CHANNEL_NAME = config['SLACK_CHANNEL_NAME']
                        env.ENVIRONMENT = config['ENVIRONMENT']
                        env.CODE_BASE_PATH = config['CODE_BASE_PATH']
                        env.ACTION_MESSAGE = config['ACTION_MESSAGE']
                        env.KEEP_APPROVAL_STAGE = config['KEEP_APPROVAL_STAGE']

                        echo "Environment      : ${env.ENVIRONMENT}"
                        echo "Code Base Path   : ${env.CODE_BASE_PATH}"
                        echo "Slack Channel    : ${env.SLACK_CHANNEL_NAME}"
                        echo "Approval Enabled : ${env.KEEP_APPROVAL_STAGE}"
                        echo "Action Message   : ${env.ACTION_MESSAGE}"
                    }
                }
            }

            stage('User Approval') {

                when {
                    expression {
                        return env.KEEP_APPROVAL_STAGE?.toBoolean()
                    }
                }

                steps {

                    echo "=========================================="
                    echo " Stage 2: User Approval"
                    echo "=========================================="

                    input(
                        message: "Deploy Redis to ${env.ENVIRONMENT} environment?",
                        ok: "Proceed"
                    )
                }
            }

            stage('Playbook Execution') {
                steps {

                    echo "=========================================="
                    echo " Stage 3: Playbook Execution"
                    echo "=========================================="

                    sh '''
                        set -e

                        echo "Checking Ansible version..."
                        ansible-playbook --version

                        echo ""
                        echo "Checking Dynamic Inventory..."
                        ansible-inventory \
                            -i aws_ec2.yml \
                            --graph

                        echo ""
                        echo "Checking Redis target connectivity..."
                        ansible \
                            env_prod \
                            -i aws_ec2.yml \
                            -m ping

                        echo ""
                        echo "Executing Redis Ansible Playbook..."
                        ansible-playbook \
                            -i aws_ec2.yml \
                            playbook.yml

                        echo ""
                        echo "Redis deployment completed successfully."
                    '''
                }
            }
        }

        post {

            success {

                echo "=========================================="
                echo " Stage 4: Notification - SUCCESS"
                echo "=========================================="

                slackSend(
                    channel: "#${env.SLACK_CHANNEL_NAME}",
                    color: "good",
                    message: """${env.ACTION_MESSAGE}

Redis deployment completed successfully.

Environment: ${env.ENVIRONMENT}
Job: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
Status: SUCCESS
"""
                )
            }

            failure {

                echo "=========================================="
                echo " Notification - FAILURE"
                echo "=========================================="

                slackSend(
                    channel: "#${env.SLACK_CHANNEL_NAME}",
                    color: "danger",
                    message: """Redis deployment failed.

Environment: ${env.ENVIRONMENT}
Job: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
Status: FAILURE
"""
                )
            }

            aborted {

                echo "=========================================="
                echo " Notification - ABORTED"
                echo "=========================================="

                slackSend(
                    channel: "#${env.SLACK_CHANNEL_NAME}",
                    color: "warning",
                    message: """Redis deployment was aborted.

Environment: ${env.ENVIRONMENT}
Job: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
Status: ABORTED
"""
                )
            }
        }
    }
}


