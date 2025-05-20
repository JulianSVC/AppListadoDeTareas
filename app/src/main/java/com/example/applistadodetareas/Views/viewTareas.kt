package com.example.applistadodetareas.Views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applistadodetareas.Data.Task
import com.example.applistadodetareas.Data.TaskDatabase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = TaskDatabase.getDatabase(application).taskDao()

    fun getTasksByUser(userId: String): Flow<List<Task>> = dao.getTasksByUser(userId)

    fun addTask(task: Task, userId: String) = viewModelScope.launch {
        dao.insertTask(task.copy(userId = userId))
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        dao.deleteTask(task)
    }


}