package com.cuncisboss.simplehabittracker.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cuncisboss.simplehabittracker.model.GraphData


@Dao
interface GraphDao {

    @Insert
    suspend fun insertGraph(graphData: GraphData)

    @Update
    suspend fun updateGraph(graphData: GraphData)

    @Delete
    suspend fun deleteGraph(graphData: GraphData)

    @Query("SELECT * FROM graph_table")
    fun getGraphs(): LiveData<List<GraphData>>

}