# 버그 수정 이력 (Bugfix Log)

## 1. Retrofit Path 어노테이션 누락
- **발생 조건**: 영화 상세 정보를 요청할 때.
- **원인**: `TmdbApiService` 인터페이스에서 `@Path` 임포트가 누락됨.
- **수정**: `import retrofit2.http.Path` 추가 및 파라미터 적용.

## 2. Navigation 인수 타입 오류
- **발생 조건**: 영화 상세 화면으로 이동할 때.
- **원인**: `movieId`를 String으로 처리하려 했으나 API 및 상세 화면에서는 Int가 필요함.
- **수정**: `navArgument` 타입을 `NavType.IntType`으로 명시하고 ViewModel에서 Int로 수신하도록 수정.

## 3. Room Flow 관찰 문제
- **발생 조건**: 리뷰 추가 후 목록이 즉시 갱신되지 않음.
- **원인**: ViewModel에서 Flow를 제대로 수집(StateFlow로 변환)하지 않음.
- **수정**: `stateIn`을 사용하여 UI에서 관찰 가능한 `StateFlow`로 변환하여 실시간 갱신 구현.
