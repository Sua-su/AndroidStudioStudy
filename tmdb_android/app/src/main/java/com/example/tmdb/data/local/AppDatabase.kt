package com.example.tmdb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tmdb.model.Achievement
import com.example.tmdb.model.Post
import com.example.tmdb.model.Review

@Database(entities = [Review::class, Post::class, Achievement::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
    abstract fun postDao(): PostDao
    abstract fun achievementDao(): AchievementDao
}
