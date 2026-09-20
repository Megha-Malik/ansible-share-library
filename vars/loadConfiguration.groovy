def call() {

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
