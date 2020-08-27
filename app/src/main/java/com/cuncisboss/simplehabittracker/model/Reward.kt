package com.cuncisboss.simplehabittracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "reward_table")
@Parcelize
data class Reward(
    val name: String,
    val nominal: Long,
    var status: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
): Parcelable