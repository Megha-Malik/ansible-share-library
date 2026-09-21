
def call() {

    pipeline {

        agent any

        stages {

            stage('Clone') {
                steps {
                    CloneStage()
                }
            }

            stage('Load Configuration') {
                steps {
                    loadConfiguration()
                }
            }

            stage('User Approval') {
                steps {
                    approvalStage()
                }
            }

            stage('Playbook Execution') {
                steps {
                    playbookStage()
                }
            }
        }

        post {

            success {
                successNotification()
            }

            failure {
                failureNotification()
            }

            aborted {
                abortedNotification()
            }
        }
    }
}
