package com.cuncisboss.simplehabittracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cuncisboss.simplehabittracker.model.Task


@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class HabitTrackerDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}