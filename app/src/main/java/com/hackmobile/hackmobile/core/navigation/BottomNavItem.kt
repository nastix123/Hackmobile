package com.example.hotelhackapp.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    data object Main : BottomNavItem(NavRoutes.Main.route, Icons.Default.Home, "Главная")
    data object Control : BottomNavItem(NavRoutes.Control.route, Icons.Default.Settings, "Управление")
}


