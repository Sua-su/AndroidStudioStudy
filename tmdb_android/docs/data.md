# 데이터 모델 명세서 (Data Model Specification)

## 1. ERD 개요
본 앱은 TMDB API로부터 가져오는 원격 데이터와 사용자가 직접 생성하는 로컬 데이터를 결합하여 사용한다.

## 2. 테이블 정의 (Room Database)

### `Review` 테이블
사용자가 작성한 영화 리뷰 정보를 저장한다.

| 필드명 | 타입 | 설명 | 제약 조건 |
| :--- | :--- | :--- | :--- |
| `id` | Long | 리뷰 고유 ID | Primary Key, Auto-increment |
| `movieId` | Int | TMDB 영화 ID | Not Null |
| `movieTitle` | String | 영화 제목 | Not Null |
| `rating` | Float | 별점 (0.5 ~ 5.0) | Not Null |
| `comment` | String | 리뷰 내용 | Not Null |
| `createdAt` | Long | 작성일 (Timestamp) | Not Null |

### `Favorite` 테이블 (Optional)
사용자가 찜한 영화 목록을 저장한다.

| 필드명 | 타입 | 설명 | 제약 조건 |
| :--- | :--- | :--- | :--- |
| `movieId` | Int | TMDB 영화 ID | Primary Key |
| `title` | String | 영화 제목 | Not Null |
| `posterPath` | String | 포스터 이미지 경로 | |
| `addedAt` | Long | 추가일 (Timestamp) | Not Null |

### `Post` 테이블
사용자가 작성한 커뮤니티 게시글 정보를 저장한다.

| 필드명 | 타입 | 설명 | 제약 조건 |
| :--- | :--- | :--- | :--- |
| `id` | Long | 게시글 고유 ID | Primary Key, Auto-increment |
| `title` | String | 제목 | Not Null |
| `content` | String | 내용 | Not Null |
| `authorNickname` | String | 작성자 닉네임 | Nullable (Anonymous) |
| `createdAt` | Long | 작성일 (Timestamp) | Not Null |

### `Achievement` 테이블
사용자의 활동 업적 및 뱃지 정보를 저장한다.

| 필드명 | 타입 | 설명 | 제약 조건 |
| :--- | :--- | :--- | :--- |
| `id` | String | 업적 고유 ID | Primary Key |
| `name` | String | 업적 이름 | Not Null |
| `description` | String | 업적 설명 | Not Null |
| `iconResId` | String | 아이콘 리소스 정보 | Not Null |
| `isUnlocked` | Boolean | 달성 여부 | Default: false |
| `unlockedAt` | Long | 달성 일시 | Nullable |

## 3. 원격 데이터 모델 (TMDB API)

### `Movie` (DTO)
- `id`: Int
- `title`: String
- `overview`: String
- `poster_path`: String
- `release_date`: String
- `vote_average`: Float

## 4. 관계 설명
- `Review`와 `Movie`는 `movieId`를 통해 논리적으로 연결된다.
- 한 영화에 대해 사용자는 여러 개의 리뷰를 남길 수 있는 구조이나, 본 앱에서는 영화당 1개의 본인 리뷰를 관리하는 것을 기본으로 한다 (수정 가능).
