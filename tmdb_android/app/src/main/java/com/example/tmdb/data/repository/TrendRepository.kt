package com.example.tmdb.data.repository

import com.example.tmdb.data.remote.TmdbApiService
import com.example.tmdb.model.MovieTrend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrendRepository @Inject constructor(
    private val apiService: TmdbApiService
) {
    private val apiKey = "c12ed457b94399d3c810d10b94e4e4c5"

    suspend fun getBoxOfficeTrends(): List<MovieTrend> = withContext(Dispatchers.IO) {
        try {
            // 1. 네이버 박스오피스 크롤링 (순수 타이틀 수집)
            val url = "https://search.naver.com/search.naver?query=박스오피스"
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .get()
            
            val items = doc.select(".item:has(.title_box), div.item") 
            
            val scrapedTitles = items.mapNotNull { element ->
                val title = element.select(".name, .title_box .name").text()
                if (title.isNotBlank()) title else null
            }.distinct()
            
            // 2. TMDB API와 병렬 매핑하여 상세 정보(TMDB ID, 줄거리, 포스터) 획득
            val mappedTrends = scrapedTitles.map { title ->
                async {
                    try {
                        val searchResult = apiService.searchMovies(apiKey, title).results.firstOrNull()
                        if (searchResult != null) {
                            val posterUrl = searchResult.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
                            MovieTrend(
                                title = searchResult.title ?: title,
                                rank = "",
                                image = posterUrl,
                                tmdbId = searchResult.id,
                                overview = searchResult.overview ?: "상세 줄거리가 제공되지 않습니다."
                            )
                        } else null
                    } catch (e: Exception) { null }
                }
            }.awaitAll().filterNotNull().toMutableList()

            // 3. 네이버 크롤링 결과가 20개 미만일 경우 TMDB 인기 영화로 목록 채우기 (20개 보장)
            if (mappedTrends.size < 20) {
                try {
                    val popularMovies = apiService.getPopularMovies(apiKey, language = "ko-KR", page = 1).results
                    for (movie in popularMovies) {
                        if (mappedTrends.size >= 20) break
                        val isDuplicate = mappedTrends.any { it.tmdbId == movie.id || it.title == movie.title }
                        if (!isDuplicate) {
                            val posterUrl = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
                            mappedTrends.add(
                                MovieTrend(
                                    title = movie.title ?: "제목 없음",
                                    rank = "",
                                    image = posterUrl,
                                    tmdbId = movie.id,
                                    overview = movie.overview ?: "상세 줄거리가 제공되지 않습니다."
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    // 무시하고 넘어감
                }
            }

            // 4. 최종 순위(Rank) 부여 및 20개 자르기
            val finalTrends = mappedTrends.take(20).mapIndexed { index, trend ->
                trend.copy(rank = (index + 1).toString())
            }

            if (finalTrends.isEmpty()) getDummyTrends() else finalTrends
        } catch (e: Exception) {
            getDummyTrends()
        }
    }
    
    private fun getDummyTrends(): List<MovieTrend> {
        return (1..20).map { 
            MovieTrend(
                title = "영화 $it", 
                rank = it.toString(), 
                image = null, 
                tmdbId = null, 
                overview = "네트워크 오류로 상세 정보를 가져올 수 없습니다."
            ) 
        }
    }
}
