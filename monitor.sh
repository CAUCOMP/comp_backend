#!/bin/bash

# 🔍 COMP Backend 모니터링 스크립트
# EC2에서 주기적으로 실행하여 애플리케이션 상태 확인

set -e

echo "📊 COMP Backend Monitoring Report"
echo "=================================="
echo "Timestamp: $(date)"
echo ""

# 1. 컨테이너 상태 확인
echo "🐳 Docker Container Status:"
docker ps -a --filter "name=comp-backend-app" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
echo ""

# 2. 컨테이너 리소스 사용량
echo "💻 Resource Usage:"
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}" comp-backend-app 2>/dev/null || echo "Container not running"
echo ""

# 3. Health Check
echo "🏥 Health Check:"
if curl -f http://localhost:8080/api/health 2>/dev/null; then
    echo "✅ Application is healthy"
else
    echo "❌ Application health check failed"
fi
echo ""

# 4. 최근 로그 (마지막 10줄)
echo "📝 Recent Logs (last 10 lines):"
docker logs --tail 10 comp-backend-app 2>/dev/null || echo "No logs available"
echo ""

# 5. 디스크 사용량
echo "💾 Disk Usage:"
df -h / | tail -n 1
echo ""

# 6. Docker 이미지 목록
echo "📦 Docker Images:"
docker images --filter "reference=*/comp-backend" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"
echo ""

# 7. 네트워크 연결 확인 (RDS & ElastiCache)
echo "🌐 Network Check:"

# RDS 연결 확인 (환경변수에서 호스트 추출)
if [ -f "/home/ubuntu/.env" ]; then
    source /home/ubuntu/.env 2>/dev/null

    # DB_URL에서 호스트 추출 (jdbc:mysql://HOST:PORT/DB 형식)
    DB_HOST=$(echo $DB_URL | sed -n 's|.*://\([^:]*\):.*|\1|p')
    REDIS_HOST_VAR=$REDIS_HOST

    if [ ! -z "$DB_HOST" ]; then
        echo -n "RDS MySQL ($DB_HOST): "
        nc -zv -w 3 $DB_HOST 3306 2>&1 | grep -q succeeded && echo "✅ Connected" || echo "❌ Not connected"
    else
        echo "RDS MySQL: ⚠️  DB_URL not configured"
    fi

    if [ ! -z "$REDIS_HOST_VAR" ]; then
        echo -n "ElastiCache Redis ($REDIS_HOST_VAR): "
        nc -zv -w 3 $REDIS_HOST_VAR ${REDIS_PORT:-6379} 2>&1 | grep -q succeeded && echo "✅ Connected" || echo "❌ Not connected"
    else
        echo "ElastiCache Redis: ⚠️  REDIS_HOST not configured"
    fi
else
    echo "⚠️  .env file not found - skipping external service checks"
fi
echo ""

echo "=================================="
echo "✅ Monitoring report completed"

