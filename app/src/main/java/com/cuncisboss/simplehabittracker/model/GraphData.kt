package com.cuncisboss.simplehabittracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "graph_table")
data class GraphData(
    var taskFinished: Int,
    var day: String,
    var datetime: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)