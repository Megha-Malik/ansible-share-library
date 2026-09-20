def call() {

    if (env.KEEP_APPROVAL_STAGE?.toBoolean()) {

        echo "=========================================="
        echo " Stage 2: User Approval"
        echo "=========================================="

        input(
            message: "Deploy Redis to ${env.ENVIRONMENT} environment?",
            ok: "Proceed"
        )
    }
}
