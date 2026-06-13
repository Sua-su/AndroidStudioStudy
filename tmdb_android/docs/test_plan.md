# 테스트 전략 및 계획 (Test Plan)

## 1. 테스트 전략
- **단위 테스트 (Unit Test)**: ViewModel의 비즈니스 로직 및 Repository의 데이터 처리 검증.
- **UI 테스트 (Compose Test)**: 주요 화면의 컴포넌트 표시 및 상호작용 검증.

## 2. 테스트 케이스 목록

### TC-01: 영화 목록 로드
- **목적**: API로부터 영화 목록을 정상적으로 가져와 화면에 표시하는지 확인.
- **방법**: `HomeViewModel`의 `movies` 상태가 비어있지 않은지 검증.

### TC-02: 리뷰 작성 및 저장
- **목적**: 사용자가 리뷰를 입력하고 저장했을 때 DB에 반영되는지 확인.
- **방법**: `DetailViewModel`의 `addReview` 실행 후 `reviews` 리스트에 포함되는지 확인.

### TC-03: 리뷰 삭제
- **목적**: 삭제 버튼 클릭 시 리뷰가 정상적으로 제거되는지 확인.
- **방법**: `deleteReview` 호출 후 해당 리뷰가 리스트에서 사라지는지 검증.

## 3. 실행 방법
- 단위 테스트: `./gradlew test`
- UI 테스트: `./gradlew connectedAndroidTest`
