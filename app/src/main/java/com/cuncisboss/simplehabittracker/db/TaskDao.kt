package com.cuncisboss.simplehabittracker.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cuncisboss.simplehabittracker.model.Task


@Dao
interface TaskDao {

    @Insert
    suspend fun addTask(task: Task)

    @Delete
    suspend fun removeTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task_table WHERE type =:type")
    fun getTasks(type: Int): LiveData<List<Task>>

    @Query("DELETE FROM task_table")
    suspend fun removeAllTask()

    @Query("DELETE FROM task_table WHERE type =:type")
    suspend fun removeTaskByType(type: Int)

    @Query("UPDATE task_table SET date=:datetime, type =:newType WHERE type =:oldType")
    suspend fun updateAllTaskByDate(datetime: String, oldType: Int, newType: Int)
}