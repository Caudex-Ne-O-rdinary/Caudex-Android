package com.cmc.caudex.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable data object SplashRoute
@Serializable data object RoomCreateRoute
@Serializable data object RoomRoute

@Serializable
data class PlantRegisterRoute(
    val gardenId: String = "",
    val isGuestRoom: Boolean = false,
)

@Serializable
data class PlantLocateRoute(
    val gardenId: String = "",
    val isGuestRoom: Boolean = false,
    val imagePath: String,
    val plantName: String,
    val managementTip: String,
    val diary: String,
)

@Serializable
data class PlantFriendRoute(val plantId: Int, val isMine: Boolean = false)

@Serializable
data class GuestRoomRoute(val gardenId: String)
