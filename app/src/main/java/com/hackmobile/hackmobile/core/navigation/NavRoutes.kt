package com.example.hotelhackapp.core.navigation

sealed class NavRoutes(val route: String) {
    data object Main : NavRoutes("main")
    data object RoomDetail : NavRoutes("room_detail/{roomId}") {
        fun createRoute(roomId: String) = "room_detail/$roomId"
    }

    data object Control : NavRoutes("control")
}
