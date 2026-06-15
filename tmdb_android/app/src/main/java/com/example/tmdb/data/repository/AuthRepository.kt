package com.example.tmdb.data.repository

import android.content.SharedPreferences
import com.example.tmdb.data.local.UserDao
import com.example.tmdb.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun signup(username: String, passwordHash: String, nickname: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser != null) {
                return@withContext Result.failure(Exception("Username already exists"))
            }

            val newUser = User(username = username, passwordHash = passwordHash, nickname = nickname)
            val userId = userDao.insertUser(newUser)
            Result.success(newUser.copy(id = userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(username: String, passwordHash: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val user = userDao.loginUser(username, passwordHash)
            if (user != null) {
                // Save session
                sharedPreferences.edit()
                    .putLong("loggedInUserId", user.id)
                    .putString("nickname", user.nickname)
                    .apply()
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid username or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        sharedPreferences.edit()
            .remove("loggedInUserId")
            .remove("nickname")
            .apply()
    }

    fun getLoggedInUserId(): Long? {
        val id = sharedPreferences.getLong("loggedInUserId", -1L)
        return if (id == -1L) null else id
    }
}
