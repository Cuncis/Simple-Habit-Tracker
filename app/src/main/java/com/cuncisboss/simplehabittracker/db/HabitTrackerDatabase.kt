package com.cuncisboss.simplehabittracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cuncisboss.simplehabittracker.model.GraphData
import com.cuncisboss.simplehabittracker.model.Reward
import com.cuncisboss.simplehabittracker.model.Task
import com.cuncisboss.simplehabittracker.model.User


@Database(
    entities = [
        Task::class,
        Reward::class,
        User::class,
        GraphData::class
    ],
    version = 6,
    exportSchema = false
)
abstract class HabitTrackerDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun rewardDao(): RewardDao

    abstract fun userDao(): UserDao

    abstract fun graphDao(): GraphDao

}