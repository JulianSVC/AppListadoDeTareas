package com.example.applistadodetareas.Views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applistadodetareas.Data.Task
import com.example.applistadodetareas.Data.TaskDatabase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = TaskDatabase.getDatabase(application).taskDao()


    // Estado interno
    private val _currentFilter = MutableStateFlow(TaskFilter.ALL)
    private val _searchQuery = MutableStateFlow("")

    // Usuario actual (deberías obtenerlo de tu sistema de autenticación)
    private val currentUserId = "user1" // Reemplaza esto con tu lógica real

    // Tareas filtradas y buscadas
    val tasks: Flow<List<Task>> = combine(
        _currentFilter,
        _searchQuery
    ) { filter, query ->
        Pair(filter, query)
    }.flatMapLatest { (filter, query) ->
        when {
            query.isNotEmpty() -> dao.searchTasksByTitle(currentUserId, "%$query%")
            filter == TaskFilter.ALL -> dao.getTasksByUser(currentUserId)
            filter == TaskFilter.COMPLETED -> dao.getCompletedTasksByUser(currentUserId)
            else -> dao.getActiveTasksByUser(currentUserId)
        }
    }

    // Cambiar filtro
    fun setFilter(filter: TaskFilter) {
        _currentFilter.value = filter
    }

    // Actualizar query de búsqueda
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Añadir nueva tarea
    fun addTask(title: String, description: String? = null) {  // Cambia a String?
        viewModelScope.launch {
            val newTask = Task(
                title = title,
                description = description,  // Acepta null
                userId = currentUserId,
                isCompleted = false,
                createdAt = System.currentTimeMillis()
            )
            dao.insertTask(newTask)
        }
    }

    // Eliminar tarea
    fun deleteTask(task: Task) = viewModelScope.launch {
        dao.deleteTask(task)
    }

    // Actualizar tarea (para edición)
    fun updateTask(task: Task) = viewModelScope.launch {
        dao.updateTask(task)
    }

    // Cambiar estado de completado (versión optimizada)
    fun toggleTaskCompletion(taskId: Int, completed: Boolean) = viewModelScope.launch {
        dao.updateTaskCompletionStatus(taskId, currentUserId, completed)
    }

    // Alternativa para cambiar estado usando el objeto completo
    fun toggleTaskCompletion(task: Task) = viewModelScope.launch {
        val updated = task.copy(isCompleted = !task.isCompleted)
        dao.updateTask(updated)
    }

    fun getTaskById(taskId: Int): Flow<Task?> {
        return flow {
            emit(dao.getTaskById(taskId, currentUserId))
        }
}

}
// Enum para los filtros
enum class TaskFilter {
    ALL, COMPLETED, ACTIVE
}