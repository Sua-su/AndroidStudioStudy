# TMRB Android (The Movie Review Board)

Agentic Coding 기반 Android Native 앱 과제 프로젝트입니다.  
TMDB API를 활용하여 영화 정보를 조회하고, 사용자가 리뷰를 작성 및 관리할 수 있는 기능을 제공합니다.

## 1. 프로젝트 개요
- **플랫폼**: Android Native (Kotlin + Jetpack Compose)
- **주요 기능**: 영화 검색 및 상세 조회, 리뷰 CRUD, 커뮤니티 게시판, 실시간 트렌드 수집(크롤링), 게이미피케이션(업적 시스템).
- **아키텍처**: MVVM
- **데이터 저장**: Room (로컬), TMDB API (원격)

## 2. 주요 기능 및 스크린
- **홈 화면 (HomeScreen)**: 인기 영화 및 현재 상영 중인 영화 목록 제공.
- **상세 화면 (DetailScreen)**: 영화의 상세 정보와 함께 리뷰 작성/수정/삭제 기능 제공.
- **검색 화면 (SearchScreen)**: 영화 제목을 통한 검색 기능.
- **게시판 (BoardScreen)**: 사용자 간 자유로운 소통이 가능한 로컬 게시판.
- **트렌드 화면 (TrendScreen)**: Jsoup을 활용한 외부 데이터(박스오피스 순위 등) 크롤링 정보 표시.
- **프로필 화면 (ProfileScreen)**: 닉네임 설정 및 획득한 업적/뱃지 확인.

## 3. 기술 스택
- **언어**: Kotlin
- **UI**: Jetpack Compose, Material 3
- **의존성 주입**: Hilt
- **네트워크**: Retrofit, OkHttp, Jsoup (Crawling)
- **데이터베이스**: Room
- **이미지 로딩**: Coil
- **비동기 처리**: Coroutines, Flow

## 4. 빌드 및 실행 방법
### 사전 요구사항
- Android Studio Ladybug 이상 권장
- JDK 17 이상
- TMDB API Key (필요 시 `local.properties` 또는 `AppModule` 등에 설정)

### 빌드 절차
1. 본 저장소를 클론합니다.
2. Android Studio에서 프로젝트를 엽니다.
3. Gradle Sync가 완료될 때까지 기다립니다.
4. `Run` 버튼을 눌러 에뮬레이터 또는 실기기에서 앱을 실행합니다.

## 5. 테스트 실행 방법
### 단위 테스트 (Unit Tests)
```bash
./gradlew test
```
- ViewModel 및 Repository 로직 검증.

### UI 테스트 (Instrumented Tests)
```bash
./gradlew connectedAndroidTest
```
- Compose UI 컴포넌트 및 화면 전환 검증.

## 6. 문서 (docs/)
상세한 설계 및 개발 기록은 `docs/` 폴더 내의 문서를 참조하십시오.
- [요구사항 명세서](docs/requirements.md)
- [데이터 모델 설계](docs/data.md)
- [개발 체크리스트](docs/checklist.md)
- [테스트 계획서](docs/test_plan.md)
- [테스트 결과 보고서](docs/test_result.md)
- [구현 이력](docs/implementation_log.md)
- [버그 수정 이력](docs/bugfix_log.md)
- [화면 목업 (HTML)](docs/ui/index.html)

---
본 프로젝트는 AI 에이전트와 협업하여 기획부터 구현, 테스트까지 수행된 과제 결과물입니다.
