package com.cmc.caudex.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cmc.caudex.presentation.plant.friend.PlantFriendScreen
import com.cmc.caudex.presentation.plant.locate.PlantLocateScreen
import com.cmc.caudex.presentation.plant.register.PlantRegisterScreen
import com.cmc.caudex.presentation.room.RoomScreen
import com.cmc.caudex.presentation.room.create.RoomCreateScreen
import com.cmc.caudex.presentation.room.guest.GuestRoomScreen
import com.cmc.caudex.presentation.splash.SplashScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SplashRoute) {
        composable<SplashRoute> {
            SplashScreen(
                onNavigateToRoomCreate = {
                    navController.navigate(RoomCreateRoute) {
                        popUpTo<SplashRoute> { inclusive = true }
                    }
                },
                onNavigateToRoom = {
                    navController.navigate(RoomRoute) {
                        popUpTo<SplashRoute> { inclusive = true }
                    }
                },
            )
        }
        composable<RoomCreateRoute> {
            RoomCreateScreen(
                onNavigateToNext = {
                    navController.navigate(RoomRoute) {
                        popUpTo<RoomCreateRoute> { inclusive = true }
                    }
                },
            )
        }
        composable<RoomRoute> {
            RoomScreen(
                onNavigateToPlantRegister = { gardenId ->
                    navController.navigate(PlantRegisterRoute(gardenId = gardenId))
                },
                onNavigateToPlantFriend = { plantId, isMine ->
                    navController.navigate(PlantFriendRoute(plantId, isMine))
                },
            )
        }
        composable<PlantRegisterRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PlantRegisterRoute>()
            PlantRegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLocate = { route -> navController.navigate(route) },
                gardenId = route.gardenId,
                isGuestRoom = route.isGuestRoom,
            )
        }
        composable<PlantLocateRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PlantLocateRoute>()
            PlantLocateScreen(
                onNavigateBack = { navController.popBackStack() },
                onSubmitSuccess = {
                    if (route.isGuestRoom) {
                        navController.popBackStack(GuestRoomRoute(route.gardenId), inclusive = false)
                    } else {
                        navController.popBackStack(RoomRoute, inclusive = false)
                    }
                },
            )
        }
        composable<PlantFriendRoute> {
            PlantFriendScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNext = { navController.popBackStack() },
            )
        }
        composable<GuestRoomRoute> {
            val route = it.toRoute<GuestRoomRoute>()
            GuestRoomScreen(
                onNavigateToPlantRegister = {
                    navController.navigate(
                        PlantRegisterRoute(
                            gardenId = route.gardenId,
                            isGuestRoom = true,
                        )
                    )
                },
                onNavigateToPlantFriend = { plantId, isMine ->
                    navController.navigate(PlantFriendRoute(plantId, isMine))
                },
            )
        }
    }
}
