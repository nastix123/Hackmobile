package com.hackmobile.hackmobile

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.hotelhackapp.presentation.MainListViewModel
import com.hackmobile.hackmobile.presentation.MainAppContent
import com.hackmobile.hackmobile.ui.theme.HackmobileTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.all { it.value }
            if (granted) {
                // Разрешения даны, можно запускать работу с Bluetooth
                showToast("Bluetooth permissions granted")
            } else {
                showToast("Bluetooth permissions denied")
            }
        }

        // Запускаем запрос разрешений сразу после регистрации
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN // добавь, если нужно
                )
            )
        } else {
            // На старых версиях Android разрешения не нужны
            showToast("Bluetooth permissions not required on this Android version")
        }

        setContent {
            HackmobileTheme {
                // Твой UI
                MainAppContent()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

