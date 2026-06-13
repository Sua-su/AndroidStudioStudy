package com.example.tmdb.di

import android.content.Context
import androidx.room.Room
import com.example.tmdb.data.local.AchievementDao
import com.example.tmdb.data.local.AppDatabase
import com.example.tmdb.data.local.PostDao
import com.example.tmdb.data.local.ReviewDao
import com.example.tmdb.data.remote.TmdbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTmdbApiService(): TmdbApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tmdb_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideReviewDao(database: AppDatabase): ReviewDao {
        return database.reviewDao()
    }

    @Provides
    fun providePostDao(database: AppDatabase): PostDao {
        return database.postDao()
    }

    @Provides
    fun provideAchievementDao(database: AppDatabase): AchievementDao {
        return database.achievementDao()
    }
}
