package com.example.applistadodetareas.Data

enum class TaskFilter {
    ALL,       // Todas las tareas
    COMPLETED, // Solo tareas completadas
    ACTIVE;    // Solo tareas pendientes

    // Propiedad de extensión para obtener el nombre legible
    val displayName: String
        get() = when (this) {
            ALL -> "Todas"
            COMPLETED -> "Completadas"
            ACTIVE -> "Pendientes"
        }

    // Método para obtener la consulta SQL correspondiente
    fun getQuery(userId: String): String {
        return when (this) {
            ALL -> "SELECT * FROM tasks WHERE userId = '$userId' ORDER BY createdAt DESC"
            COMPLETED -> "SELECT * FROM tasks WHERE userId = '$userId' AND isCompleted = 1 ORDER BY createdAt DESC"
            ACTIVE -> "SELECT * FROM tasks WHERE userId = '$userId' AND isCompleted = 0 ORDER BY createdAt DESC"
        }
    }

    // Método para filtrar una lista local (sin consultar la base de datos)
    fun applyFilter(tasks: List<Task>): List<Task> {
        return when (this) {
            ALL -> tasks
            COMPLETED -> tasks.filter { it.isCompleted }
            ACTIVE -> tasks.filter { !it.isCompleted }
        }
    }

    companion object {
        // Método para obtener el filtro por defecto
        fun default(): TaskFilter = ALL

        // Método para parsear desde string (útil para guardar en preferencias)
        fun fromString(value: String): TaskFilter {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                default()
            }
        }
    }
}