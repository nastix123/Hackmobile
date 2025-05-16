package com.hackmobile.hackmobile.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hotelhackapp.core.navigation.BottomNavItem
import com.example.hotelhackapp.core.navigation.NavRoutes
import com.hackmobile.hackmobile.presentation.control.RoomControlScreen
import com.hackmobile.hackmobile.presentation.detail.RoomDetailScreen
import com.hackmobile.hackmobile.presentation.main.MainScreen
import com.hackmobile.hackmobile.presentation.main.NoReservationPlaceholder
import com.hackmobile.hackmobile.presentation.main.RoomDetailScreenV2
import com.hackmobile.hackmobile.presentation.main.RoomListScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.Main.route) {
        composable(NavRoutes.Main.route) {
            RoomListScreen(navController)
//            MainScreen(navController = navController)
        }
        composable(BottomNavItem.Control.route) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                RoomControlScreen()
            }
        }

        composable(
            route = NavRoutes.RoomDetail.route,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            RoomDetailScreenV2(roomId = roomId, navController = navController)
//            RoomDetailScreen(roomId = roomId, navController = navController)
        }

        composable(NavRoutes.Control.route) {
            val hasReservation = remember { mutableStateOf(true) } // Подставь логику из ViewModel
            if (hasReservation.value) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    RoomControlScreen()
                }
            } else {
                NoReservationPlaceholder()
            }
        }
    }
}