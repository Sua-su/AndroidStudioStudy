package com.example.a1901153test.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.a1901153test.ui.viewmodel.MovieDetailViewModel

// 이미지 URL 하드코딩 (목록 화면이랑 똑같이 또 씀 - 초보자 스타일)
private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun MovieDetailScreen(
    navController: NavController,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // 상단 바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navController.popBackStack() }
            ) {
                Text(text = "← 뒤로", color = Color.White)
            }

            Text(
                text = "영화 상세정보",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
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

        // 에러
        if (viewModel.errorMessage.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = viewModel.errorMessage, color = Color.Red)
            }
            return@Column
        }

        // 영화 정보 없음
        if (viewModel.movie == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "영화 정보가 없습니다.")
            }
            return@Column
        }

        val movie = viewModel.movie!!

        // 스크롤 가능하게
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 포스터 이미지 (큰 사이즈)
            AsyncImage(
                model = "$IMAGE_BASE_URL${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {

                // 영화 제목
                Text(
                    text = movie.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // 태그라인 있으면 표시
                if (movie.tagline.isNotEmpty()) {
                    Text(
                        text = "\"${movie.tagline}\"",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Italic
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 평점
                val rating = String.format("%.1f", movie.voteAverage)
                Text(
                    text = "⭐ $rating / 10  (${movie.voteCount}명 평가)",
                    fontSize = 16.sp,
                    color = Color(0xFFFFA000),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 개봉일
                Row {
                    Text(text = "개봉일: ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = movie.releaseDate, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 상영시간 - null이면 "미정" 표시
                Row {
                    Text(text = "상영시간: ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    val runtime = if (movie.runtime != null) "${movie.runtime}분" else "미정"
                    Text(text = runtime, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 장르 목록 (콤마로 이어붙이기)
                if (movie.genres.isNotEmpty()) {
                    Row {
                        Text(text = "장르: ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        val genreNames = movie.genres.joinToString(", ") { it.name }
                        Text(text = genreNames, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 상태
                if (movie.status.isNotEmpty()) {
                    Row {
                        Text(text = "상태: ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = movie.status, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(12.dp))

                // 줄거리
                Text(
                    text = "줄거리",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (movie.overview.isEmpty()) {
                    Text(
                        text = "줄거리 정보가 없습니다.",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                } else {
                    Text(
                        text = movie.overview,
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
