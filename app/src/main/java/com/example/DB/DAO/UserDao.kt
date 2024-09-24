package com.example.DB.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.DB.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>

    @Insert
    suspend fun insert(user: User)
}