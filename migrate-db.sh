#!/bin/bash

# 데이터베이스 마이그레이션 스크립트
# 사용법: ./migrate-db.sh [환경]
# 환경: local, dev, prod (기본값: local)

set -e

ENV=${1:-local}
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SQL_FILE="$SCRIPT_DIR/compdb.sql"

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}데이터베이스 마이그레이션 시작${NC}"
echo -e "${GREEN}환경: $ENV${NC}"
echo -e "${GREEN}========================================${NC}"

# 환경별 설정
case $ENV in
  local)
    DB_HOST="localhost"
    DB_PORT="3306"
    DB_NAME="comp"
    DB_USER="comp"
    echo -e "${YELLOW}로컬 환경에서 마이그레이션을 진행합니다.${NC}"
    ;;
  dev)
    echo -e "${YELLOW}개발 환경 설정을 입력하세요.${NC}"
    read -p "DB Host: " DB_HOST
    read -p "DB Port (기본값: 3306): " DB_PORT
    DB_PORT=${DB_PORT:-3306}
    read -p "DB Name (기본값: comp): " DB_NAME
    DB_NAME=${DB_NAME:-comp}
    read -p "DB User: " DB_USER
    ;;
  prod)
    echo -e "${RED}⚠️  프로덕션 환경에서 마이그레이션을 진행하려고 합니다!${NC}"
    read -p "정말로 진행하시겠습니까? (yes/no): " CONFIRM
    if [ "$CONFIRM" != "yes" ]; then
      echo -e "${YELLOW}마이그레이션이 취소되었습니다.${NC}"
      exit 0
    fi
    read -p "RDS Endpoint: " DB_HOST
    read -p "DB Port (기본값: 3306): " DB_PORT
    DB_PORT=${DB_PORT:-3306}
    read -p "DB Name (기본값: comp): " DB_NAME
    DB_NAME=${DB_NAME:-comp}
    read -p "DB User: " DB_USER
    ;;
  *)
    echo -e "${RED}알 수 없는 환경: $ENV${NC}"
    echo "사용법: ./migrate-db.sh [local|dev|prod]"
    exit 1
    ;;
esac

# SQL 파일 존재 확인
if [ ! -f "$SQL_FILE" ]; then
  echo -e "${RED}오류: SQL 파일을 찾을 수 없습니다: $SQL_FILE${NC}"
  exit 1
fi

# 백업 확인 (프로덕션인 경우)
if [ "$ENV" == "prod" ]; then
  echo -e "${YELLOW}백업을 먼저 수행하는 것을 권장합니다.${NC}"
  read -p "백업을 수행하시겠습니까? (yes/no): " BACKUP
  if [ "$BACKUP" == "yes" ]; then
    BACKUP_FILE="backup_${DB_NAME}_$(date +%Y%m%d_%H%M%S).sql"
    echo -e "${GREEN}백업 중... $BACKUP_FILE${NC}"
    mysqldump -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p "$DB_NAME" > "$BACKUP_FILE"
    echo -e "${GREEN}백업 완료: $BACKUP_FILE${NC}"
  fi
fi

# 데이터베이스 생성 확인
echo -e "${YELLOW}데이터베이스를 생성하시겠습니까? (이미 있다면 skip)${NC}"
read -p "(yes/no): " CREATE_DB
if [ "$CREATE_DB" == "yes" ]; then
  echo -e "${GREEN}데이터베이스 생성 중...${NC}"
  mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p -e "CREATE DATABASE IF NOT EXISTS $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
  echo -e "${GREEN}데이터베이스 생성 완료${NC}"
fi

# 마이그레이션 실행
echo -e "${GREEN}마이그레이션 실행 중...${NC}"
mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p "$DB_NAME" < "$SQL_FILE"

if [ $? -eq 0 ]; then
  echo -e "${GREEN}========================================${NC}"
  echo -e "${GREEN}✅ 마이그레이션 성공!${NC}"
  echo -e "${GREEN}========================================${NC}"

  # 테이블 목록 확인
  echo -e "${GREEN}생성된 테이블 목록:${NC}"
  mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p "$DB_NAME" -e "SHOW TABLES;"

  echo ""
  echo -e "${YELLOW}다음 단계:${NC}"
  echo "1. User 테이블 구조 확인: DESCRIBE User;"
  echo "2. Spring Boot 애플리케이션 설정 확인"
  echo "3. 애플리케이션 실행 및 연결 테스트"
else
  echo -e "${RED}========================================${NC}"
  echo -e "${RED}❌ 마이그레이션 실패${NC}"
  echo -e "${RED}========================================${NC}"
  exit 1
fi

