def call() {

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
