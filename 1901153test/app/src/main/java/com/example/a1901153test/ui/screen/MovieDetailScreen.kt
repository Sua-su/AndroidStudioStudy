package com.example.a1901153test.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.a1901153test.ui.viewmodel.MovieDetailViewModel

// 포스터를 불러오기 위한 베이스 주소에요!
private const val IMG_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun MovieDetailScreen(
    navController: NavController, 
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 바 부분입니다! 뒤로 가는 버튼과 제목이 있어요.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2))
                .padding(8.dp), 
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "← 뒤로" 버튼! 누르면 popBackStack()이 호출되어 이전 화면으로 가요.
            Button(onClick = { navController.popBackStack() }) { 
                Text("← 뒤로") 
            }
            Text(
                text = "영화 상세정보", 
                color = Color.White, 
                fontSize = 18.sp, 
                fontWeight = FontWeight.Bold, 
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // 지금 정보를 가져오는 상태인지, 에러인지 체크합니다!
        if (viewModel.isLoading == true) {
            // 로딩 중일 때는 화면 한가운데서 뱅글뱅글!
            Box(
                modifier = Modifier.fillMaxSize(), 
                contentAlignment = Alignment.Center
            ) { 
                CircularProgressIndicator() 
            }
        } 
        else if (viewModel.errorMessage.isEmpty() == false) {
            // 에러가 났을 때는 에러 메시지를 빨간색으로 보여줍니다.
            Box(
                modifier = Modifier.fillMaxSize(), 
                contentAlignment = Alignment.Center
            ) { 
                Text(
                    text = viewModel.errorMessage, 
                    color = Color.Red
                ) 
            }
        } 
        else {
            // 드디어 정보를 다 가져왔다면?
            val movieData = viewModel.movie
            
            // 만약 movieData가 null이 아니라면 정보를 그립니다!
            // 초보자가 알기 쉽게 if문으로 널(null) 체크를 명시적으로 해줬어요.
            if (movieData != null) {
                // 내용이 화면보다 길 수도 있으니 수직 스크롤을 가능하게 만듭니다.
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // 이 설정이 있어야 아래로 스크롤 됩니다!
                        .padding(16.dp), 
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 영화 상세 포스터 이미지!
                    AsyncImage(
                        model = IMG_URL + movieData.posterPath, 
                        contentDescription = "상세 포스터", 
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp), 
                        contentScale = ContentScale.Fit // 이미지 비율을 깨뜨리지 않고 화면에 맞춰요.
                    )
                    
                    // 영화 제목
                    Text(
                        text = movieData.title, 
                        fontSize = 24.sp, 
                        fontWeight = FontWeight.Bold
                    )
                    
                    // 영화의 부제목(태그라인)이 있다면 기울임꼴로 보여줍니다!
                    if (movieData.tagline.isEmpty() == false) {
                        Text(
                            text = "\"" + movieData.tagline + "\"", 
                            fontStyle = FontStyle.Italic, 
                            color = Color.Gray
                        )
                    }
                    
                    // 평점과 참여 인원 수
                    Text(text = "평점: ⭐ " + movieData.voteAverage + " / 10 (" + movieData.voteCount + "명 참여)")
                    
                    // 개봉일과 상영 시간
                    Text(text = "개봉일: " + movieData.releaseDate + " | 상영시간: " + movieData.runtime + "분")
                    
                    // 장르 리스트를 하나로 합쳐서 보여줍니다.
                    val genreList = movieData.genres.map { it.name }.joinToString(", ")
                    Text(text = "장르: " + genreList)
                    
                    // 가로선 하나 긋기!
                    HorizontalDivider() 
                    
                    // 줄거리 부분
                    Text(
                        text = "줄거리", 
                        fontSize = 18.sp, 
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = movieData.overview, 
                        lineHeight = 22.sp // 가독성을 위해 줄 간격을 살짝 넓혔어요.
                    )
                    
                    // 맨 아래가 너무 바짝 붙지 않게 여유 공간을 줍니다.
                    Spacer(modifier = Modifier.height(20.dp)) 
                }
            } else {
                // 혹시라도 데이터를 다 가져왔는데 값이 비어있다면?
                Box(
                    modifier = Modifier.fillMaxSize(), 
                    contentAlignment = Alignment.Center
                ) { 
                    Text("영화 정보가 존재하지 않습니다.") 
                }
            }
        }
    }
}
