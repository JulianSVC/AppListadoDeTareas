package com.example.applistadodetareas.Data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val userId: String = "", // Añade este campo para vincular tareas al usuario
    val createdAt: Long = System.currentTimeMillis()
)