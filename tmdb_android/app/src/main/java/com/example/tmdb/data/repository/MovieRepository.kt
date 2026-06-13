package com.example.tmdb.data.repository

import com.example.tmdb.data.local.AchievementDao
import com.example.tmdb.data.local.PostDao
import com.example.tmdb.data.local.ReviewDao
import com.example.tmdb.data.remote.TmdbApiService
import com.example.tmdb.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val apiService: TmdbApiService,
    private val reviewDao: ReviewDao,
    private val postDao: PostDao,
    private val achievementDao: AchievementDao
) {
    private val apiKey = "c12ed457b94399d3c810d10b94e4e4c5"

    init {
        // 초기 업적 데이터 삽입
        kotlinx.coroutines.GlobalScope.launch {
            achievementDao.insertAchievements(listOf(
                Achievement("first_review", "첫 리뷰 작성", "첫 번째 영화 리뷰를 작성했습니다.", "stars"),
                Achievement("movie_buff_5", "영화 마니아 (5)", "영화 리뷰 5개를 작성했습니다.", "movie"),
                Achievement("movie_buff_10", "영화 전문가 (10)", "영화 리뷰 10개를 작성했습니다.", "workspace_premium")
            ))
        }
    }

    suspend fun getPopularMovies(): List<Movie> {
        return apiService.getPopularMovies(apiKey).results.map { it.toDomain() }
    }

    suspend fun searchMovies(query: String): List<Movie> {
        return apiService.searchMovies(apiKey, query).results.map { it.toDomain() }
    }

    suspend fun getMovieDetails(movieId: Int): Movie {
        return apiService.getMovieDetails(movieId, apiKey).toDomain()
    }

    fun getAllReviews(): Flow<List<Review>> = reviewDao.getAllReviews()

    fun getReviewsForMovie(movieId: Int): Flow<List<Review>> = reviewDao.getReviewsForMovie(movieId)

    suspend fun addReview(review: Review) {
        reviewDao.insertReview(review)
        checkAchievements()
    }

    private suspend fun checkAchievements() {
        val reviewCount = reviewDao.getAllReviews().first().size
        if (reviewCount >= 1) achievementDao.unlockAchievement("first_review")
        if (reviewCount >= 5) achievementDao.unlockAchievement("movie_buff_5")
        if (reviewCount >= 10) achievementDao.unlockAchievement("movie_buff_10")
    }

    fun getAllAchievements(): Flow<List<Achievement>> = achievementDao.getAllAchievements()

    suspend fun updateReview(review: Review) = reviewDao.updateReview(review)

    suspend fun deleteReview(review: Review) = reviewDao.deleteReview(review)

    // Board / Post
    fun getAllPosts(): Flow<List<Post>> = postDao.getAllPosts()

    suspend fun addPost(post: Post) = postDao.insertPost(post)

    suspend fun updatePost(post: Post) = postDao.updatePost(post)

    suspend fun deletePost(post: Post) = postDao.deletePost(post)
}
