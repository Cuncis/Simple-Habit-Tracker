package com.cuncisboss.simplehabittracker.ui.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuncisboss.simplehabittracker.db.TaskDao
import com.cuncisboss.simplehabittracker.model.Task
import com.niwattep.materialslidedatepicker.SlideDatePickerDialogViewModel
import kotlinx.coroutines.launch

class TodoViewModel(private val taskDao: TaskDao) : ViewModel() {

    fun addTask(task: Task) = viewModelScope.launch {
        taskDao.addTask(task)
    }

    fun removeTask(task: Task) = viewModelScope.launch {
        taskDao.removeTask(task)
    }

    fun getTasks(type: Int): LiveData<List<Task>> {
        return taskDao.getTasks(type)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.updateTask(task)
    }

    fun removeAllTask() = viewModelScope.launch {
        taskDao.removeAllTask()
    }

}