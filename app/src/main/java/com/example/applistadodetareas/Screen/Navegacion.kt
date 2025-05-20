package com.example.applistadodetareas.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applistadodetareas.Screen.Login.LoginScreen
import com.example.applistadodetareas.Screen.Login.RegisterScreen
import com.example.applistadodetareas.Screen.Tarea.AddTaskScreen
import com.example.applistadodetareas.Screen.Tarea.TaskListScreen
import com.example.applistadodetareas.Views.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = Firebase.auth


    NavHost(
        navController = navController,
        startDestination = if (auth.currentUser != null) "taskList" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("taskList") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("taskList") },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable("taskList") {
            auth.currentUser?.uid?.let { userId ->
                TaskListScreen(
                    userId = userId,
                    navController = navController
                )
            } ?: navController.navigate("login")
        }
        composable("addTask") {
            auth.currentUser?.uid?.let { userId ->
                AddTaskScreen(
                    userId = userId,
                    onBack = { navController.popBackStack() }
                )
            } ?: navController.navigate("login")
        }
    }



}