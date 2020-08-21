package com.cuncisboss.simplehabittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task_table")
data class Task(
    var icon: Int,
    var name: String,
    var date: String,
    var type: Int,
    var value: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)