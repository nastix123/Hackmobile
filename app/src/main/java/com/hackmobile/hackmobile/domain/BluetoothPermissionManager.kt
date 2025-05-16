package com.hackmobile.hackmobile.domain


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class BluetoothPermissionManager(
    private val context: Context,
    private val activity: ComponentActivity,
    private val onPermissionGranted: () -> Unit,
    private val onPermissionDenied: () -> Unit
) {

    @RequiresApi(Build.VERSION_CODES.S)
    private val requiredPermissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN
    )

    private val permissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun hasPermissions(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun requestPermissions() {
        if (hasPermissions()) {
            onPermissionGranted()
        } else {
            permissionLauncher.launch(requiredPermissions)
        }
    }
}