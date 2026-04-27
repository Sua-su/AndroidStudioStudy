# CLAUDE.md — Posts App V1 설계 및 구현 지침

## 프로젝트 개요

JSONPlaceholder API를 사용한 게시글 조회 앱. MVVM 아키텍처 기반.
**Hilt 미사용, Repository 미사용.** ViewModel이 Retrofit API를 직접 호출한다.

- 언어: Kotlin
- UI: Jetpack Compose + Material3
- 네트워크: Retrofit + Kotlinx Serialization
- 상태관리: ViewModel + StateFlow
- 화면이동: Navigation Compose (type-safe, @Serializable route)

---

## API

Base URL: `https://jsonplaceholder.typicode.com/`

| 엔드포인트 | 메서드 | 응답 |
|-----------|--------|------|
| `posts` | GET | `List<Post>` |
| `posts/{id}` | GET | `Post` |

Post 스키마:
```json
{
  "userId": 1,
  "id": 1,
  "title": "string",
  "body": "string"
}
```

---

## 패키지 구조

```
com.example.posts/
├── MainActivity.kt
├── data/
│   ├── model/
│   │   └── Post.kt
│   └── remote/
│       ├── PostApi.kt
│       └── RetrofitInstance.kt
├── ui/
│   ├── navigation/
│   │   ├── Routes.kt
│   │   └── AppNavHost.kt
│   ├── list/
│   │   ├── PostListViewModel.kt
│   │   └── PostListScreen.kt
│   └── detail/
│       ├── PostDetailViewModel.kt
│       └── PostDetailScreen.kt
```

---

## 구현 순서 및 각 파일 명세

아래 순서대로 파일을 생성한다. 각 단계는 이전 단계에 의존하므로 순서를 지킨다.

### Step 1: Post.kt

경로: `data/model/Post.kt`

```kotlin
@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
```

규칙:
- `kotlinx.serialization.Serializable` 사용
- 필드명은 JSON 키와 정확히 일치시킨다
- 이 클래스는 Navigation route의 argument로 전달하지 않는다. id만 전달한다.

### Step 2: PostApi.kt

경로: `data/remote/PostApi.kt`

```kotlin
interface PostApi {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): Post
}
```

규칙:
- 반환 타입에 `Call<T>`을 쓰지 않는다. suspend 함수로 직접 반환한다.
- `Response<T>` 래핑도 하지 않는다. 에러는 예외로 처리한다.

### Step 3: RetrofitInstance.kt

경로: `data/remote/RetrofitInstance.kt`

```kotlin
object RetrofitInstance {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val postApi: PostApi = retrofit.create(PostApi::class.java)
}
```

규칙:
- `object` 싱글톤으로 선언한다.
- `ignoreUnknownKeys = true` 필수.
- BASE_URL 끝에 `/` 포함.
- OkHttpClient를 별도로 만들지 않는다. Retrofit 기본 클라이언트 사용.

### Step 4: Routes.kt

경로: `ui/navigation/Routes.kt`

```kotlin
@Serializable
data object PostListRoute

@Serializable
data class PostDetailRoute(val postId: Int)
```

규칙:
- `kotlinx.serialization.Serializable` 사용 (Navigation의 type-safe route용).
- 파라미터 없는 route는 `data object`.
- 파라미터 있는 route는 `data class`. 파라미터 타입은 Primitive만.

### Step 5: PostListViewModel.kt

경로: `ui/list/PostListViewModel.kt`

UiState:
```kotlin
sealed interface PostListUiState {
    data object Loading : PostListUiState
    data class Success(val posts: List<Post>) : PostListUiState
    data class Error(val message: String) : PostListUiState
}
```

ViewModel:
- 생성자 파라미터 없음. 내부에서 `RetrofitInstance.postApi`를 직접 참조.
- `_uiState: MutableStateFlow<PostListUiState>` (private)
- `uiState: StateFlow<PostListUiState>` (public, asStateFlow())
- 초기값: `PostListUiState.Loading`
- `init` 블록에서 `loadPosts()` 호출
- `loadPosts()` 함수: public, `viewModelScope.launch`로 API 호출, try-catch로 Success/Error 분기

Factory:
- 이 ViewModel은 생성자 파라미터가 없으므로 Factory가 필요 없다.
- Screen에서 `viewModel()` 호출 시 factory 파라미터 생략.

### Step 6: PostDetailViewModel.kt

경로: `ui/detail/PostDetailViewModel.kt`

UiState:
```kotlin
sealed interface PostDetailUiState {
    data object Loading : PostDetailUiState
    data class Success(val post: Post) : PostDetailUiState
    data class Error(val message: String) : PostDetailUiState
}
```

ViewModel:
- 생성자 파라미터: `postId: Int`
- 내부에서 `RetrofitInstance.postApi`를 직접 참조.
- `_uiState`, `uiState` 패턴은 PostListViewModel과 동일.
- `init` 블록에서 `loadPost()` 호출
- `loadPost()` 함수: private, `viewModelScope.launch`로 `getPost(postId)` 호출

Factory:
- `postId`를 생성자로 받으므로 Factory 필수.
- companion object에 `fun factory(postId: Int): ViewModelProvider.Factory` 선언.
- `viewModelFactory { initializer { PostDetailViewModel(postId) } }` 패턴 사용.

### Step 7: PostListScreen.kt

경로: `ui/list/PostListScreen.kt`

```kotlin
@Composable
fun PostListScreen(
    onPostClick: (Int) -> Unit,
    viewModel: PostListViewModel = viewModel()
)
```

규칙:
- Navigation 콜백은 `onPostClick: (Int) -> Unit`으로 받는다. navController를 직접 참조하지 않는다.
- `viewModel.uiState`를 `collectAsStateWithLifecycle()`으로 구독.
- `when` 분기로 Loading/Success/Error 처리.
- Loading: `CircularProgressIndicator` (중앙 배치)
- Success: `LazyColumn`으로 게시글 목록. 각 항목은 `Card` + `clickable`.
  - title은 `titleMedium`, maxLines 1, ellipsis.
  - body는 `bodySmall`, maxLines 2, ellipsis.
  - 항목 간 간격 8.dp, 전체 padding 16.dp.
  - `items(posts, key = { it.id })` 사용.
- Error: 에러 메시지 + "다시 시도" 버튼 (`viewModel.loadPosts()` 호출).
- TopAppBar 사용, 제목 "게시글 목록".

### Step 8: PostDetailScreen.kt

경로: `ui/detail/PostDetailScreen.kt`

```kotlin
@Composable
fun PostDetailScreen(
    postId: Int,
    onBack: () -> Unit,
    viewModel: PostDetailViewModel = viewModel(
        factory = PostDetailViewModel.factory(postId)
    )
)
```

규칙:
- `postId`를 파라미터로 받아 Factory에 전달.
- `onBack: () -> Unit`으로 뒤로가기 콜백. navController 직접 참조 금지.
- TopAppBar에 뒤로가기 아이콘 (`Icons.AutoMirrored.Filled.ArrowBack`).
- Loading/Error 처리는 PostListScreen과 동일 패턴.
- Success: `verticalScroll`로 스크롤 가능한 상세 내용.
  - title: `headlineSmall`
  - 작성자 ID 표시: `"작성자 ID: ${post.userId}"`
  - `HorizontalDivider`로 구분
  - body: `bodyLarge`

### Step 9: AppNavHost.kt

경로: `ui/navigation/AppNavHost.kt`

```kotlin
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PostListRoute
    ) {
        composable<PostListRoute> {
            PostListScreen(
                onPostClick = { postId ->
                    navController.navigate(PostDetailRoute(postId = postId))
                }
            )
        }
        composable<PostDetailRoute> { backStackEntry ->
            val route: PostDetailRoute = backStackEntry.toRoute()
            PostDetailScreen(
                postId = route.postId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
```

규칙:
- `navController`는 이 파일에서만 존재. Screen에 전달하지 않는다.
- `startDestination`에 문자열이 아닌 route 객체 사용.
- `backStackEntry.toRoute<T>()`로 파라미터 추출.

### Step 10: MainActivity.kt

경로: `MainActivity.kt` (패키지 루트)

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavHost()
                }
            }
        }
    }
}
```

규칙:
- 커스텀 Theme 없이 `MaterialTheme` 기본 사용.
- `Surface`로 전체 감싸기.
- Activity에서 ViewModel을 생성하지 않는다. 각 Screen이 자체 ViewModel을 가진다.

---

## 코딩 컨벤션

- 한 파일에 한 클래스/인터페이스. 단, UiState sealed interface는 해당 ViewModel 파일에 함께 둔다.
- import 시 wildcard(`.*`) 사용 가능 (Compose 관례).
- Composable 함수명은 PascalCase.
- ViewModel의 MutableStateFlow는 private, StateFlow는 public.
- 에러 메시지: `e.message ?: "알 수 없는 오류가 발생했습니다"`
- Compose 어노테이션: Material3의 ExperimentalMaterial3Api는 `@OptIn`으로 처리.

---

## 하지 말 것

- Hilt, Dagger, Koin 등 DI 프레임워크 사용 금지.
- Repository 패턴 사용 금지. ViewModel이 RetrofitInstance.postApi를 직접 호출.
- Room, DataStore 등 로컬 저장소 사용 금지.
- `Call<T>` 사용 금지. suspend 함수만 사용.
- `Response<T>` 래핑 금지. 에러는 예외(try-catch)로 처리.
- Screen Composable에서 navController를 직접 참조 금지. 콜백으로 처리.
- 커스텀 Theme 파일 생성 금지. MaterialTheme 기본값 사용.
- OkHttpClient 커스터마이징 금지 (Interceptor, 타임아웃 등).
- Post 객체를 Navigation argument로 통째로 전달 금지. id만 전달.
