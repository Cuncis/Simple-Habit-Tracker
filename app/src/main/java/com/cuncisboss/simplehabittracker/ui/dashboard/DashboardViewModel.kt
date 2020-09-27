package com.cuncisboss.simplehabittracker.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuncisboss.simplehabittracker.db.GraphDao
import com.cuncisboss.simplehabittracker.db.UserDao
import com.cuncisboss.simplehabittracker.model.GraphData
import com.cuncisboss.simplehabittracker.model.User
import kotlinx.coroutines.launch

class DashboardViewModel(private val userDao: UserDao, private val graphDao: GraphDao): ViewModel() {

    private val _level = MutableLiveData<Int>()
    val level: LiveData<Int> = _level

    val totalLevel = MutableLiveData<Int>()

    fun setLevel(lv: Int) {
        _level.value = lv
    }

    fun insertUser(user: User) = viewModelScope.launch {
        userDao.insertUser(user)
    }

    fun updateUser(user: User) = viewModelScope.launch {
        userDao.updateUser(user)
    }

    fun showUserDetail(): LiveData<User> {
        return userDao.displayUserDetail()
    }

    fun removeUserDetail() = viewModelScope.launch {
        userDao.removeUserDetail()
    }

    fun updateUserByUsername(totalGold: Long, totalExp: Long, username: String, qty: Int) = viewModelScope.launch {
        userDao.updateUserByUsername(totalGold, totalExp, username, qty)
    }

    fun insertGraph(graphData: GraphData) = viewModelScope.launch {
        graphDao.insertGraph(graphData)
    }

    fun updateGraph(graphData: GraphData) = viewModelScope.launch {
        graphDao.updateGraph(graphData)
    }

    fun deleteGraph(graphData: GraphData) = viewModelScope.launch {
        graphDao.deleteGraph(graphData)
    }

    fun getGraphs(): LiveData<List<GraphData>> {
        return graphDao.getGraphs()
    }

}