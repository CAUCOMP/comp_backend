#!/bin/bash

# EC2 배포 디버깅 스크립트
# 사용법: ./debug-ec2.sh <EC2_IP> <PEM_KEY_PATH>

set -e

EC2_IP=$1
PEM_KEY=${2:-~/.ssh/comp-backend.pem}
EC2_USER=${3:-ubuntu}

if [ -z "$EC2_IP" ]; then
    echo "사용법: $0 <EC2_IP> [PEM_KEY_PATH] [EC2_USER]"
    echo "예시: $0 13.125.123.456 ~/.ssh/my-key.pem ubuntu"
    exit 1
fi

echo "=========================================="
echo "EC2 배포 디버깅 시작"
echo "EC2 IP: $EC2_IP"
echo "PEM Key: $PEM_KEY"
echo "User: $EC2_USER"
echo "=========================================="

ssh -i "$PEM_KEY" \
    -o StrictHostKeyChecking=no \
    -o UserKnownHostsFile=/dev/null \
    $EC2_USER@$EC2_IP << 'ENDSSH'

echo ""
echo "=== 1. 파일 존재 확인 ==="
echo "--- 홈 디렉토리 파일 목록 ---"
ls -la ~/

echo ""
echo "=== 2. .env 파일 확인 ==="
if [ -f .env ]; then
    echo "✅ .env 파일 존재"
    echo "--- 환경변수 목록 (값 숨김) ---"
    cat .env | grep -v '^#' | cut -d= -f1
else
    echo "❌ .env 파일이 없습니다!"
fi

echo ""
echo "=== 3. docker-compose.yml 확인 ==="
if [ -f docker-compose.yml ]; then
    echo "✅ docker-compose.yml 파일 존재"
    echo "--- 파일 내용 확인 ---"
    head -20 docker-compose.yml
else
    echo "❌ docker-compose.yml 파일이 없습니다!"
fi

echo ""
echo "=== 4. Docker 상태 확인 ==="
echo "--- Docker 버전 ---"
docker --version

echo ""
echo "--- Docker Compose 버전 ---"
docker-compose --version

echo ""
echo "=== 5. 실행 중인 컨테이너 ==="
docker ps -a

echo ""
echo "=== 6. Docker Compose 상태 ==="
docker-compose ps || echo "docker-compose.yml을 찾을 수 없거나 오류 발생"

echo ""
echo "=== 7. Docker 이미지 목록 ==="
docker images

echo ""
echo "=== 8. 최근 컨테이너 로그 (50줄) ==="
if docker-compose ps | grep -q "comp-backend-app"; then
    docker-compose logs --tail=50
else
    echo "컨테이너가 실행 중이지 않습니다"
fi

echo ""
echo "=== 9. 포트 8080 확인 ==="
if netstat -tlnp 2>/dev/null | grep 8080; then
    echo "✅ 포트 8080이 열려있습니다"
else
    echo "❌ 포트 8080이 열려있지 않습니다"
fi

echo ""
echo "=== 10. 로컬 Health Check ==="
if curl -f http://localhost:8080/api/health 2>/dev/null; then
    echo "✅ Health Check 성공!"
else
    echo "❌ Health Check 실패"
fi

echo ""
echo "=== 11. 디스크 사용량 ==="
df -h

echo ""
echo "=== 12. Docker 디스크 사용량 ==="
docker system df

echo ""
echo "=========================================="
echo "디버깅 완료"
echo "=========================================="

ENDSSH

echo ""
echo "=========================================="
echo "추가 확인 사항:"
echo "=========================================="
echo ""
echo "1. EC2 보안 그룹 확인:"
echo "   - 인바운드 규칙에 8080 포트 허용 여부 확인"
echo ""
echo "2. RDS 보안 그룹 확인:"
echo "   - EC2 IP에서 3306 포트 접근 허용 여부 확인"
echo ""
echo "3. ElastiCache 보안 그룹 확인:"
echo "   - EC2 IP에서 6379 포트 접근 허용 여부 확인"
echo ""
echo "4. GitHub Secrets 확인:"
echo "   - 모든 필수 Secrets가 설정되어 있는지 확인"
echo ""

