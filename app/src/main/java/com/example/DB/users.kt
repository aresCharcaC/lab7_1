package com.example.DB

import com.example.DB.DAO.UserDao
import com.example.DB.entity.User

class UserRepository(private val userDao: UserDao) {
    suspend fun getAllUsers(): List<User> = userDao.getAll()
    suspend fun insertUser(user: User) = userDao.insert(user)
}