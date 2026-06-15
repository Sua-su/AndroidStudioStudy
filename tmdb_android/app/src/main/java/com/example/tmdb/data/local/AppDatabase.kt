package com.example.tmdb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tmdb.model.Achievement
import com.example.tmdb.model.Comment
import com.example.tmdb.model.Post
import com.example.tmdb.model.Review

import com.example.tmdb.model.User

@Database(entities = [Review::class, Post::class, Achievement::class, Comment::class, User::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
    abstract fun postDao(): PostDao
    abstract fun achievementDao(): AchievementDao
    abstract fun commentDao(): CommentDao
    abstract fun userDao(): UserDao
}
