package com.cmc.caudex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import com.cmc.caudex.presentation.navigation.RoomCreateRoute
import com.cmc.caudex.presentation.navigation.SplashRoute
import com.cmc.caudex.presentation.room.create.RoomCreateScreen
import com.cmc.caudex.presentation.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaudexTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = SplashRoute) {
                    composable<SplashRoute> {
                        SplashScreen(onSplashComplete = {
                            navController.navigate(RoomCreateRoute) {
                                popUpTo<SplashRoute> { inclusive = true }
                            }
                        })
                    }
                    composable<RoomCreateRoute> {
                        RoomCreateScreen(
                            onNavigateToNext = {

                            }
                        )
                    }
                }
            }
        }
    }
}
