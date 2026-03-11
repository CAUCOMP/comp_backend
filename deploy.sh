#!/bin/bash

# 🚀 COMP Backend 배포 스크립트
# EC2에서 수동 배포 시 사용

set -e  # 에러 발생 시 스크립트 중단

echo "🚀 Starting deployment..."

# 1. Docker Hub에서 최신 이미지 pull
echo "📦 Pulling latest image from Docker Hub..."
docker pull ${DOCKERHUB_USERNAME:-your-username}/comp-backend:latest

# 2. 기존 컨테이너 중지 및 제거
echo "🛑 Stopping existing containers..."
docker-compose down || true

# 3. 새 컨테이너 시작
echo "▶️  Starting new containers..."
docker-compose up -d

# 4. 오래된 이미지 정리
echo "🧹 Cleaning up old images..."
docker image prune -af

# 5. 컨테이너 상태 확인
echo "✅ Checking container status..."
docker-compose ps

# 6. Health check
echo "🏥 Performing health check..."
sleep 10
if curl -f http://localhost:8080/api/health; then
    echo "✅ Deployment successful!"
else
    echo "❌ Health check failed. Check logs with: docker logs comp-backend-app"
    exit 1
fi

echo "🎉 Deployment completed successfully!"

