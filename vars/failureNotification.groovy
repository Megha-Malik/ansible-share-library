def call() {

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
