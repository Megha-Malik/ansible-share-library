def call() {

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
            all \
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
