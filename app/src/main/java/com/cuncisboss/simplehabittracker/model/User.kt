package com.cuncisboss.simplehabittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class User(
    val name: String = "",
    val jobTitle: String = "",
    val level: String = "",
    var gold: Long = 0L,
    var exp: Long = 0L,
    var qty: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)