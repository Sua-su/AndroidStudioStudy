package com.example.tmdb.data.repository

import com.example.tmdb.model.MovieTrend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrendRepository @Inject constructor() {

    suspend fun getBoxOfficeTrends(): List<MovieTrend> = withContext(Dispatchers.IO) {
        try {
            // 크롤링 대상: KOBIS (영화진흥위원회) 또는 네이버 영화 박스오피스
            // 여기서는 예시로 KOBIS 실시간 예매율 순위를 크롤링하는 로직을 시뮬레이션하거나 실제 Jsoup 로직 적용
            val url = "https://www.kobis.or.kr/kobis/business/main/main.do"
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()
            
            val trends = mutableListOf<MovieTrend>()
            // KOBIS 메인 페이지의 예매율 순위 선택자 정밀화
            val items = doc.select(".all_list .mov_list li, .box_office_list li") 
            
            items.take(10).forEachIndexed { index, element ->
                val title = element.select(".tit, .movie-nm").text()
                val rank = (index + 1).toString()
                var imageUrl = element.select("img").attr("abs:src")
                if (imageUrl.isBlank()) imageUrl = element.select("img").attr("abs:data-src")
                
                if (title.isNotBlank()) {
                    trends.add(MovieTrend(title, rank, imageUrl))
                }
            }
            
            // 만약 크롤링이 실패하거나 구조가 변경되었다면 더미 데이터 대신 다른 소스 시도 가능
            if (trends.isEmpty()) {
                // 더미 데이터 (데모용)
                return@withContext listOf(
                    MovieTrend("범죄도시4", "1", null),
                    MovieTrend("인사이드 아웃 2", "2", null),
                    MovieTrend("퓨리오사: 매드맥스 사가", "3", null)
                )
            }
            
            trends
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
