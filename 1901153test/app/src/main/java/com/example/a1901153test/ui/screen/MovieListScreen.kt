package com.example.a1901153test.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.a1901153test.data.model.MovieDto
import com.example.a1901153test.ui.viewmodel.MovieListViewModel

// 영화 포스터 이미지를 가져오기 위해 주소 앞에 붙여야 하는 기본 URL 주소입니다!
private const val IMG_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun MovieListScreen(
    navController: NavController, 
    viewModel: MovieListViewModel = hiltViewModel() // Hilt를 이용해 준비된 뷰모델을 가져옵니다.
) {
    // 현재 어떤 탭(0번: 전체, 1번: 인기)이 선택되었는지 기억하기 위한 변수에요.
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 화면 맨 위에 제목이 나오는 파란색 상단바를 만듭니다!
        Text(
            text = "🎬 영화 목록", 
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2))
                .padding(16.dp), 
            color = Color.White, 
            fontSize = 20.sp, 
            fontWeight = FontWeight.Bold
        )

        // 화면 중앙에서 탭을 누를 수 있게 만드는 부분입니다.
        TabRow(selectedTabIndex = selectedTab) {
            // "전체 영화" 탭
            Tab(
                selected = (selectedTab == 0), 
                onClick = { selectedTab = 0 }, // 누르면 0번 탭 선택!
                text = { Text("전체 영화") }
            )
            // "인기 영화" 탭
            Tab(
                selected = (selectedTab == 1), 
                onClick = { selectedTab = 1 }, // 누르면 1번 탭 선택!
                text = { Text("🔥 인기 영화") }
            )
        }

        // 만약 지금 데이터를 불러오는 중이라면?
        if (viewModel.isLoading == true) {
            Box(
                modifier = Modifier.fillMaxSize(), 
                contentAlignment = Alignment.Center
            ) { 
                // 화면 정중앙에 빙글빙글 도는 로딩 아이콘을 보여줍니다.
                CircularProgressIndicator() 
            }
        } 
        // 에러가 발생해서 메시지가 있다면?
        else if (viewModel.errorMessage.isEmpty() == false) {
            Box(
                modifier = Modifier.fillMaxSize(), 
                contentAlignment = Alignment.Center
            ) { 
                // 빨간색 글씨로 에러 메시지를 화면에 띄웁니다.
                Text(
                    text = viewModel.errorMessage, 
                    color = Color.Red
                ) 
            }
        } 
        // 로딩도 아니고 에러도 아니면, 데이터를 성공적으로 가져온 거에요!
        else {
            // 선택된 탭에 따라서 보여줄 영화 목록 리스트를 결정합니다.
            val movies = if (selectedTab == 0) {
                viewModel.nowPlayingMovies
            } else {
                viewModel.popularMovies
            }
            
            // 영화 목록을 리스트 형태로 쫙 그려주는 LazyColumn입니다! (리사이클러뷰 같은거)
            LazyColumn(
                modifier = Modifier.fillMaxSize(), 
                contentPadding = PaddingValues(8.dp), 
                verticalArrangement = Arrangement.spacedBy(8.dp) // 아이템들 사이에 8dp씩 간격을 줍니다.
            ) {
                // movies 리스트에 있는 영화 개수만큼 반복하면서 그려줍니다.
                items(movies) { movie ->
                    // 영화 하나하나의 카드 아이템을 만듭니다.
                    MovieItem(
                        movie = movie, 
                        onItemClick = { 
                            // 이 카드를 누르면 상세 화면으로 넘어갑니다! (아이디를 가지고 갑니다)
                            navController.navigate("movie_detail/" + movie.id) 
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: MovieDto, onItemClick: () -> Unit) {
    // 카드 모양으로 깔끔하게 감싸줍니다!
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick), // 누르면 동작하게 클릭 이벤트 연결!
        elevation = CardDefaults.cardElevation(4.dp) // 입체감을 위해 그림자를 줍니다.
    ) {
        Row(
            modifier = Modifier.padding(8.dp), 
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 인터넷 주소(URL)로 되어 있는 영화 포스터를 가져와서 보여주는 컴포넌트입니다.
            AsyncImage(
                model = IMG_URL + movie.posterPath, 
                contentDescription = "영화 포스터 이미지", 
                modifier = Modifier.size(70.dp, 100.dp), 
                contentScale = ContentScale.Crop // 사진 비율에 맞게 잘라서 가득 채웁니다.
            )
            
            // 텍스트 정보들을 세로로 나열합니다.
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f), // 남은 공간을 이 글씨들이 다 채우게 합니다.
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // 영화 제목 (너무 길면 한 줄까지만 보여주고 뒤에는 ... 처리해요)
                Text(
                    text = movie.title, 
                    fontWeight = FontWeight.Bold, 
                    maxLines = 1, 
                    overflow = TextOverflow.Ellipsis
                )
                
                // 개봉일 날짜에서 앞 4글자(연도)만 가져와서 보여줍니다.
                val releaseYear = movie.releaseDate.take(4)
                Text(
                    text = "개봉: " + releaseYear + "년", 
                    fontSize = 12.sp, 
                    color = Color.Gray
                )
                
                // 평점을 소수점 1자리까지만 예쁘게 포맷팅해서 별이랑 같이 보여줍니다.
                val ratingText = String.format("%.1f", movie.voteAverage)
                Text(
                    text = "⭐ " + ratingText + " / 10", 
                    fontSize = 13.sp, 
                    color = Color(0xFFFFA000)
                )
                
                // 줄거리 요약 (두 줄까지만 보여줍니다)
                Text(
                    text = movie.overview, 
                    fontSize = 11.sp, 
                    maxLines = 2, 
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
