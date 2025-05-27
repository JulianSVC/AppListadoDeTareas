package com.example.applistadodetareas.Screen

import AddTaskScreen
import LoginScreen
import RegisterScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applistadodetareas.Screen.Tarea.EditTaskScreen
import com.example.applistadodetareas.Screen.Tarea.TaskListScreen
import com.example.applistadodetareas.Views.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = Firebase.auth
    var isUserLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    // Observar cambios en la autenticación
    LaunchedEffect(auth) {
        auth.addAuthStateListener { firebaseAuth ->
            isUserLoggedIn = firebaseAuth.currentUser != null
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) "taskList" else "login"
    ) {
        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("taskList") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // Pantalla de Registro
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("taskList") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // Lista de Tareas
        composable("taskList") {
            auth.currentUser?.uid?.let { userId ->
                TaskListScreen(
                    userId = userId,
                    navController = navController,
                    onLogout = {
                        auth.signOut()
                        navController.navigate("login") {
                            popUpTo("taskList") { inclusive = true }
                        }
                    }
                )
            } ?: navController.navigate("login")
        }

        // Añadir Nueva Tarea
        composable("addTask") {
            auth.currentUser?.uid?.let { userId ->
                AddTaskScreen(
                    userId = userId,
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel()
                )
            } ?: navController.navigate("login")
        }

        // Editar Tarea Existente
        composable(
            route = "editTask/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            auth.currentUser?.uid?.let { userId ->
                EditTaskScreen(
                    taskId = backStackEntry.arguments?.getInt("taskId") ?: -1,
                    navController = navController,
                    viewModel = viewModel()
                )
            } ?: navController.navigate("login")
        }
    }
}