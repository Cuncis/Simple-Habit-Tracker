package com.cuncisboss.simplehabittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class User(
    val name: String,
    val jobTitle: String,
    val level: String,
    val gold: Long,
    val exp: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)