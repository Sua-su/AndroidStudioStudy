package com.example.a1901153test.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

// 포스터 이미지 기본 URL (하드코딩)
private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun MovieListScreen(
    navController: NavController,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    // 0 = 전체(현재상영), 1 = 인기
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        // 상단 타이틀 바 (하드코딩 색상)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "🎬 영화 목록",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 탭
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("전체 영화") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("🔥 인기 영화") }
            )
        }

        // 로딩 중
        if (viewModel.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Column
        }

        // 에러 메시지
        if (viewModel.errorMessage.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewModel.errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
            return@Column
        }

        // 탭에 따라 보여줄 영화 목록
        val moviesToShow = if (selectedTab == 0) {
            viewModel.nowPlayingMovies
        } else {
            viewModel.popularMovies
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(moviesToShow) { movie ->
                MovieItem(
                    movie = movie,
                    onClick = {
                        // 영화 상세 화면으로 이동
                        navController.navigate("movie_detail/${movie.id}")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MovieItem(movie: MovieDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 영화 포스터
            AsyncImage(
                model = "$IMAGE_BASE_URL${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier.size(width = 70.dp, height = 100.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 개봉일 (앞 4자리만 잘라서 연도만 표시 - 초보자 스타일)
                val year = if (movie.releaseDate.length >= 4) {
                    movie.releaseDate.substring(0, 4)
                } else {
                    "미정"
                }
                Text(
                    text = "개봉: $year",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 평점을 소수점 1자리로 반올림
                val rating = String.format("%.1f", movie.voteAverage)
                Text(
                    text = "⭐ $rating / 10",
                    fontSize = 13.sp,
                    color = Color(0xFFFFA000)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.overview,
                    fontSize = 11.sp,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
