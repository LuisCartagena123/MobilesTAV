package com.example.evaluacion2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.evaluacion2.viewmodel.AppViewModel
import androidx.activity.viewModels
import com.example.evaluacion2.data.RoomRepository
import com.example.evaluacion2.ui.theme.Evaluacion2Theme
import com.example.evaluacion2.views.AppNavigation

class MainActivity : ComponentActivity() {
    private val roomRepo by lazy { RoomRepository(applicationContext) }
    private val appViewModel: AppViewModel by viewModels { AppViewModel.provideFactory(roomRepo) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Evaluacion2Theme {
                MyApp(appViewModel)
            }
        }
    }
}

@Composable
fun MyApp(appViewModel: AppViewModel) {
    val navController = rememberNavController()
    MaterialTheme {
        AppNavigation(navController, appViewModel)
    }
}