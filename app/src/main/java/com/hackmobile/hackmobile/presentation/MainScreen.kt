package com.hackmobile.hackmobile.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hackmobile.hackmobile.core.navigation.AppNavGraph
import com.hackmobile.hackmobile.core.navigation.BottomNavigationBar
import com.example.hotelhackapp.utils.AppColors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainAppContent(
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(Modifier
            .fillMaxSize()
            .padding(padding)
            .background(AppColors.Background)
        ) {
            AppNavGraph(navController = navController)
        }
    }
}