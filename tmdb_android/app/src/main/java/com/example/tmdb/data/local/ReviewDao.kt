package com.example.tmdb.data.local

import androidx.room.*
import com.example.tmdb.model.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews ORDER BY createdAt DESC")
    fun getAllReviews(): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE authorNickname IS :nickname ORDER BY createdAt DESC")
    fun getReviewsByUser(nickname: String?): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE authorId = :userId ORDER BY createdAt DESC")
    fun getReviewsByUserId(userId: Long): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE movieId = :movieId")
    fun getReviewsForMovie(movieId: Int): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)

    @Update
    suspend fun updateReview(review: Review)

    @Delete
    suspend fun deleteReview(review: Review)
}
