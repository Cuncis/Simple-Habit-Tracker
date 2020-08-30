package com.cuncisboss.simplehabittracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cuncisboss.simplehabittracker.model.User


@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user_table")
    fun displayUserDetail(): LiveData<User>

    @Query("DELETE FROM user_table")
    suspend fun removeUserDetail()

}