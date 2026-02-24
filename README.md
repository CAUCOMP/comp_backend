# comp_backend
comp 웹페이지 BE

## 🛠 Tech Stack
- **Language**: Java 21
- **Framework**: Spring Boot 4.0.2
- **Database**: MySQL 8.0
- **Cache**: Redis 7
- **Build Tool**: Gradle 8.x
- **ORM**: Spring Data JPA
- **Security**: Spring Security

## 📋 Prerequisites
- Java 21+
- Docker & Docker Compose
- Gradle 8.x (또는 Gradle Wrapper 사용)

## 🚀 Getting Started

### 0. 개발 환경 설정 (필수!)
IntelliJ IDEA를 사용하는 경우, **자동 포맷팅 설정이 필수**입니다.
자세한 설정 방법은 [SETUP.md](SETUP.md)를 참조하세요.

**요약:**
1. `Settings` → `Editor` → `Code Style` → ✅ "Enable EditorConfig support"
2. `Settings` → `Tools` → `Actions on Save` → ✅ "Reformat code" 체크

### 1. Clone the repository
```bash
git clone <repository-url>
cd comp_backend
```

### 2. Start Docker containers (MySQL & Redis)
```bash
docker-compose up -d
```

### 3. Run the application
```bash
./gradlew bootRun
```

애플리케이션은 `http://localhost:8080`에서 실행됩니다.

## 🗄 Database Configuration

### MySQL
- **Host**: localhost
- **Port**: 3306
- **Database**: comp
- **Username**: comp
- **Password**: comp2026!

### Redis
- **Host**: localhost
- **Port**: 6379
- **Password**: comp2026!

## 🏗 Build Commands

```bash
# 의존성 다운로드 및 빌드
./gradlew build

# 테스트 실행
./gradlew test

# 코드 포맷팅 체크
./gradlew spotlessCheck

# 코드 포맷팅 자동 적용
./gradlew spotlessApply

# Checkstyle 실행
./gradlew checkstyleMain checkstyleTest

# 애플리케이션 실행
./gradlew bootRun

# JAR 파일 생성
./gradlew bootJar
```

## 🔄 CI/CD Pipeline

dev 브랜치에 PR을 올리면 다음 단계가 자동으로 실행됩니다:

1. **의존성 다운로드**: Gradle 캐시 + 빌드 단계에서 의존성 자동 다운로드
2. **코드 생성**: `compileJava` 실행 (코드 생성 태스크 포함)
3. **Lint**: `spotlessCheck` (코드 포맷팅) + `checkstyle` (코드 스타일 검사)
4. **Type Check**: `compileJava`, `compileTestJava` (컴파일 검증)
5. **Unit Tests**: `test` (H2 인메모리 DB로 빠른 테스트 실행)
6. **Build**: `bootJar` (실행 가능한 JAR 파일 생성)

### CI 워크플로우 상태
모든 체크가 통과해야 PR을 머지할 수 있습니다.

**참고**: CI 환경에서는 H2 인메모리 데이터베이스를 사용하여 테스트를 실행합니다. MySQL/Redis 서비스 컨테이너를 띄울 필요가 없어 빌드 속도가 빠릅니다.

## 📁 Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── comp/
│   │           └── comp_web/
│   │               ├── CompWebApplication.java
│   │               ├── domain/          # 도메인 모델
│   │               ├── controller/      # REST 컨트롤러
│   │               ├── service/         # 비즈니스 로직
│   │               ├── repository/      # 데이터 접근 레이어
│   │               └── config/          # 설정 클래스
│   └── resources/
│       └── application.properties       # 애플리케이션 설정
└── test/
    └── java/                            # 테스트 코드
```

## 🧪 Testing
```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests CompWebApplicationTests

# 테스트 리포트 확인
open build/reports/tests/test/index.html
```

**테스트 환경**:
- 테스트는 **H2 인메모리 데이터베이스**를 사용합니다 (빠른 테스트 실행)
- MySQL/Redis 없이도 테스트 가능
- 설정 파일: `src/test/resources/application-test.properties`
- 프로파일: `@ActiveProfiles("test")`로 활성화

## 🐳 Docker Commands
```bash
# 컨테이너 시작
docker-compose up -d

# 컨테이너 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs -f

# 컨테이너 중지
docker-compose down

# 컨테이너 중지 및 볼륨 삭제
docker-compose down -v
```

## 📝 Code Quality

### Code Formatting
프로젝트는 `.editorconfig` 파일을 사용하여 일관된 코드 스타일을 유지합니다.

#### Spotless로 포맷팅 검증
Git 커밋 전에 Spotless로 코드 스타일을 검증할 수 있습니다:

```bash
# 포맷팅 체크
./gradlew spotlessCheck

# 자동 포맷팅 적용
./gradlew spotlessApply
```

**참고**: IntelliJ의 `Actions on Save`가 활성화되어 있으면, 저장할 때마다 EditorConfig 규칙이 자동 적용되므로 `spotlessApply`를 수동으로 실행할 필요가 거의 없습니다.


### Static Analysis (Checkstyle)
코드 스타일 가이드를 준수하는지 확인합니다.

```bash
# Checkstyle 실행
./gradlew checkstyleMain checkstyleTest

# 리포트 확인
open build/reports/checkstyle/main.html
```

## 🤝 Contributing
1. dev 브랜치에서 새로운 기능 브랜치 생성
2. 변경사항 커밋
3. dev 브랜치로 PR 생성
4. CI 체크 통과 확인
5. 코드 리뷰 후 머지

## 📄 License
[License 정보 추가 필요]

