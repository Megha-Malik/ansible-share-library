def call() {

    stage('Clone') {

        echo "=========================================="
        echo " Stage 1: Clone"
        echo "=========================================="

        deleteDir()

        git(
            branch: 'main',
            url: 'https://github.com/Megha-Malik/ansible-redis.git'
        )

        echo "Repository cloned successfully."
    }
}
