package com.cuncisboss.simplehabittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task_table")
data class Task(
    val icon: Int,
    val name: String,
    val date: String,
    val type: Int,
    val value: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)