package com.cuncisboss.simplehabittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task_table")
data class Task(
    var icon: Int,
    var name: String,
    var date: String,
    var type: Int,          // yesterday: 1, today: 2, tomorrow: 3
    var gold: Long,
    var exp: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)