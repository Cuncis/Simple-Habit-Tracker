package com.cuncisboss.simplehabittracker.ui.reward

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuncisboss.simplehabittracker.db.RewardDao
import com.cuncisboss.simplehabittracker.model.Reward
import kotlinx.coroutines.launch

class RewardViewModel(private val rewardDao: RewardDao): ViewModel() {

    fun addReward(reward: Reward) = viewModelScope.launch {
        rewardDao.addReward(reward)
    }

    fun removeReward(reward: Reward) = viewModelScope.launch {
        rewardDao.removeReward(reward)
    }

    fun getAllRewards(): LiveData<List<Reward>> {
        return rewardDao.getAllRewards()
    }

    fun updateReward(reward: Reward) = viewModelScope.launch {
        rewardDao.updateReward(reward)
    }
}