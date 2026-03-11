#!/bin/bash

# 환경변수 로드 헬퍼 스크립트
# 사용법: source load-env.sh

if [ ! -f .env ]; then
    echo "❌ .env 파일이 존재하지 않습니다."
    echo "📝 다음 명령어로 .env 파일을 생성하세요:"
    echo "   cp .env.example .env"
    echo "   # .env 파일을 열어 실제 값으로 수정"
    return 1
fi

echo "🔄 .env 파일에서 환경변수를 로드합니다..."

# .env 파일의 각 라인을 읽어서 환경변수로 export
set -a
source .env
set +a

echo "✅ 환경변수 로드 완료!"
echo ""
echo "📊 현재 설정된 환경변수:"
echo "   DB_URL: ${DB_URL}"
echo "   DB_USERNAME: ${DB_USERNAME}"
echo "   DB_PASSWORD: ${DB_PASSWORD:0:3}***"
echo "   REDIS_HOST: ${REDIS_HOST}"
echo "   REDIS_PORT: ${REDIS_PORT}"
echo "   REDIS_PASSWORD: ${REDIS_PASSWORD:0:3}***"
echo "   JWT_SECRET: ${JWT_SECRET:0:10}***"
echo "   SERVER_PORT: ${SERVER_PORT}"
echo ""
echo "🚀 이제 './gradlew bootRun' 명령어로 애플리케이션을 실행할 수 있습니다."

