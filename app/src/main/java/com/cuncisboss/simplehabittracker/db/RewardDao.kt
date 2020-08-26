package com.cuncisboss.simplehabittracker.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cuncisboss.simplehabittracker.model.Reward

@Dao
interface RewardDao {

    @Insert
    suspend fun addReward(reward: Reward)

    @Delete
    suspend fun removeReward(reward: Reward)

    @Query("SELECT * FROM reward_table")
    fun getAllRewards(): LiveData<List<Reward>>

    @Update
    suspend fun updateReward(reward: Reward)

}