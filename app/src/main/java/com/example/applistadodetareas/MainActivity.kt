package com.example.applistadodetareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.applistadodetareas.Screen.AppNavigation
import com.example.applistadodetareas.ui.theme.AppListadoDeTareasTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase explícitamente
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            AppListadoDeTareasTheme {
                AppNavigation()
            }
        }
    }
}