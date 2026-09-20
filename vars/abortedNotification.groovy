def call() {

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
