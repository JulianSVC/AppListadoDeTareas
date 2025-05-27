package com.example.applistadodetareas.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // Obtener todas las tareas de un usuario
    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTasksByUser(userId: String): Flow<List<Task>>

    // Obtener tareas completadas
    @Query("SELECT * FROM tasks WHERE userId = :userId AND isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTasksByUser(userId: String): Flow<List<Task>>

    // Obtener tareas pendientes
    @Query("SELECT * FROM tasks WHERE userId = :userId AND isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveTasksByUser(userId: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId AND userId = :userId LIMIT 1")
    suspend fun getTaskById(taskId: Int, userId: String): Task?
    // Insertar nueva tarea
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    // Eliminar tarea
    @Delete
    suspend fun deleteTask(task: Task)

    // Actualizar tarea (para ediciones generales)
    @Update
    suspend fun updateTask(task: Task)

    // Método optimizado para cambiar estado de completado
    @Query("UPDATE tasks SET isCompleted = :completed WHERE id = :taskId AND userId = :userId")
    suspend fun updateTaskCompletionStatus(taskId: Int, userId: String, completed: Boolean)

    // Buscar tareas por título
    @Query("SELECT * FROM tasks WHERE userId = :userId AND title LIKE :query ORDER BY createdAt DESC")
    fun searchTasksByTitle(userId: String, query: String): Flow<List<Task>>
}