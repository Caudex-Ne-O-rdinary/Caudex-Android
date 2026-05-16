package com.cmc.caudex

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cmc.caudex.presentation.designsystem.theme.CaudexTheme
import com.cmc.caudex.presentation.navigation.AppNavGraph
import com.cmc.caudex.presentation.navigation.GardenInviteLink
import com.cmc.caudex.presentation.navigation.GuestRoomRoute
import com.cmc.caudex.presentation.navigation.SplashRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
        )
        setContent {
            CaudexTheme {
                val controller = rememberNavController()
                navController = controller

                AppNavGraph(navController = controller)

                LaunchedEffect(controller) {
                    handleDeepLink(intent, controller)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        navController?.let { handleDeepLink(intent, it) }
    }

    private fun handleDeepLink(intent: Intent, navController: NavController) {
        val uri = intent.data ?: return
        if (uri.scheme == "https") {
            val gardenId = GardenInviteLink.extractGardenId(uri.toString()).takeIf { it.isNotBlank() } ?: return

            navController.navigate(GuestRoomRoute(gardenId)) {
                popUpTo<SplashRoute> { inclusive = true }
            }
        }
    }
}
