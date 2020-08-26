package com.cuncisboss.simplehabittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reward_table")
data class Reward(
    val name: String,
    val nominal: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)