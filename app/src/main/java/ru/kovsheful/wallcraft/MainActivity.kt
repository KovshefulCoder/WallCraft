package ru.kovsheful.wallcraft

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.presentation.home.home
import ru.kovsheful.wallcraft.ui.theme.Background
import ru.kovsheful.wallcraft.ui.theme.WallCraftCleanArchitectureTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge(
//            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
//        )
        setContent {
            WallCraftCleanArchitectureTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background
                ) {
                    WallCraftApp()
                }
            }
        }
    }
}

@Composable
fun WallCraftApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        home()
    }
}
