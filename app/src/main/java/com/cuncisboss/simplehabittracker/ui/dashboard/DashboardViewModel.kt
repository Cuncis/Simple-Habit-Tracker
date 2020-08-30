package com.cuncisboss.simplehabittracker.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuncisboss.simplehabittracker.db.UserDao
import com.cuncisboss.simplehabittracker.model.User
import kotlinx.coroutines.launch

class DashboardViewModel(private val userDao: UserDao): ViewModel() {

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

}