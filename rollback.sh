#!/bin/bash

# 🔄 COMP Backend 롤백 스크립트
# 배포 실패 시 이전 버전으로 롤백

set -e

echo "🔄 Starting rollback process..."

# 롤백할 이미지 태그 입력받기
if [ -z "$1" ]; then
    echo "Usage: ./rollback.sh <image-tag>"
    echo "Example: ./rollback.sh main-abc1234"
    echo ""
    echo "Available tags:"
    docker images ${DOCKERHUB_USERNAME:-your-username}/comp-backend --format "{{.Tag}}"
    exit 1
fi

ROLLBACK_TAG=$1
DOCKERHUB_USERNAME=${DOCKERHUB_USERNAME:-your-username}
IMAGE_NAME="${DOCKERHUB_USERNAME}/comp-backend:${ROLLBACK_TAG}"

echo "📦 Rolling back to: $IMAGE_NAME"

# 1. 이미지 존재 확인
if ! docker pull $IMAGE_NAME; then
    echo "❌ Error: Image $IMAGE_NAME not found in Docker Hub"
    exit 1
fi

# 2. docker-compose.yml 백업
echo "💾 Backing up current docker-compose.yml..."
cp docker-compose.yml docker-compose.yml.backup

# 3. 이미지 태그 변경
echo "✏️  Updating docker-compose.yml..."
sed -i.bak "s|image:.*comp-backend:.*|image: $IMAGE_NAME|g" docker-compose.yml

# 4. 컨테이너 재시작
echo "🔄 Restarting containers..."
docker-compose down
docker-compose up -d

# 5. Health check
echo "🏥 Performing health check..."
sleep 15
for i in {1..10}; do
    if curl -f http://localhost:8080/api/health 2>/dev/null; then
        echo "✅ Rollback successful!"
        echo "🗑️  Cleaning up backup files..."
        rm -f docker-compose.yml.bak
        exit 0
    fi
    echo "Waiting for application to start... ($i/10)"
    sleep 10
done

# 롤백 실패 시 원래 설정으로 복구
echo "❌ Rollback failed. Restoring original configuration..."
mv docker-compose.yml.backup docker-compose.yml
docker-compose down
docker-compose up -d

echo "⚠️  Rollback failed. Original configuration restored."
exit 1

