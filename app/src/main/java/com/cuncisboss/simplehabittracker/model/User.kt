package com.cuncisboss.simplehabittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class User(
    val name: String = "",
    val jobTitle: String = "",
    val level: String = "",
    var gold: Long = 0L,
    val exp: Long = 0L,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)