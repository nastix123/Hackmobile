package com.hackmobile.hackmobile.domain

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID



class BluetoothManager(
    private val androidContext: Context
) {

    companion object {
        private const val TAG = "BluetoothManager"
        private val SERVICE_UUID = UUID.fromString("000000FF-0000-1000-8000-00805F9B34FB")  // 0x00FF в 128-битном формате
        private val CHAR_NOTIFY_UUID = UUID.fromString("0000FF01-0000-1000-8000-00805F9B34FB")  // 0xFF01
        private val CHAR_WRITE_UUID = UUID.fromString("0000FF02-0000-1000-8000-00805F9B34FB")   // 0xFF02
        private val CLIENT_CHARACTERISTIC_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")
    }

    private val _responses = MutableSharedFlow<Test.ControllerResponse>(extraBufferCapacity = 10)
    val responses = _responses.asSharedFlow()

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothGatt: BluetoothGatt? = null
    private var notifyCharacteristic: BluetoothGattCharacteristic? = null
    private var writeCharacteristic: BluetoothGattCharacteristic? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val gattCallback = object : BluetoothGattCallback() {

        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Log.d(TAG, "onConnectionStateChange: status=$status newState=$newState")
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "Connected to GATT server, discovering services...")
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "Disconnected from GATT server")
                bluetoothGatt = null
                notifyCharacteristic = null
                writeCharacteristic = null
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Services discovered")
                val service = gatt.getService(SERVICE_UUID)
                if (service == null) {
                    Log.e(TAG, "Service $SERVICE_UUID not found!")
                    return
                }

                notifyCharacteristic = service.getCharacteristic(CHAR_NOTIFY_UUID)
                writeCharacteristic = service.getCharacteristic(CHAR_WRITE_UUID)

                if (notifyCharacteristic == null || writeCharacteristic == null) {
                    Log.e(TAG, "One or both characteristics not found!")
                    return
                }

                val successNotify = gatt.setCharacteristicNotification(notifyCharacteristic, true)
                if (!successNotify) {
                    Log.e(TAG, "Failed to enable notifications on notifyCharacteristic")
                    return
                }

                val descriptor = notifyCharacteristic!!.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            } else {
                Log.e(TAG, "Service discovery failed with status $status")
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            if (descriptor.uuid == CLIENT_CHARACTERISTIC_CONFIG_UUID) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d(TAG, "Notification enabled, sending IdentifyRequest...")
                    sendIdentifyRequestInternal()
                } else {
                    Log.e(TAG, "Failed to write descriptor for notification, status=$status")
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            if (characteristic.uuid == CHAR_NOTIFY_UUID) {
                val data = characteristic.value
                try {
                    val response = Test.ControllerResponse.parseFrom(data)
                    Log.d(TAG, "Received ControllerResponse: $response")
                    coroutineScope.launch {
                        _responses.emit(response)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to parse ControllerResponse", e)
                }
            }
        }


        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            Log.d(TAG, "Characteristic write status: $status")
        }
    }

    private var pendingToken: String? = null

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    suspend fun connect(deviceName: String, token: String): Boolean = withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                androidContext,
                Manifest.permission.BLUETOOTH_CONNECT
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "BLUETOOTH_CONNECT permission not granted")
                return@withContext false
            }
        }

        val device = bluetoothAdapter
            ?.bondedDevices
            ?.firstOrNull { it.name == deviceName }
            ?: run {
                Log.e(TAG, "Device $deviceName not found")
                return@withContext false
            }

        bluetoothGatt = device.connectGatt(androidContext, false, gattCallback)
        if (bluetoothGatt == null) {
            Log.e(TAG, "Failed to connect GATT")
            return@withContext false
        }

        pendingToken = token

        delay(1000)

        return@withContext true
    }

    @SuppressLint("MissingPermission")
    private fun sendIdentifyRequestInternal() {
        val token = pendingToken ?: return
        if (writeCharacteristic == null || bluetoothGatt == null) {
            return
        }

        val identifyRequest = Test.IdentifyRequest.newBuilder()
            .setToken(token)
            .build()


        writeCharacteristic!!.value = identifyRequest.toByteArray()
        val writeSuccess = bluetoothGatt!!.writeCharacteristic(writeCharacteristic)
        Log.d(TAG, "Sent IdentifyRequest: success=$writeSuccess")
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    suspend fun sendDoorCommand(open: Boolean): Boolean = withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                androidContext,
                Manifest.permission.BLUETOOTH_CONNECT
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "BLUETOOTH_CONNECT permission not granted")
                return@withContext false
            }
        }

        if (writeCharacteristic == null || bluetoothGatt == null) {
            Log.e(TAG, "Not connected or characteristics not initialized")
            return@withContext false
        }

        val setState = Test.SetState.newBuilder()
            .setState(if (open) Test.States.DoorLockOpen else Test.States.DoorLockClose)
            .build()

        val message = Test.ClientMessage.newBuilder()
            .setSetState(setState)
            .build()

        writeCharacteristic!!.value = message.toByteArray()
        val success = bluetoothGatt!!.writeCharacteristic(writeCharacteristic)
        Log.d(TAG, "Sent door command: success=$success")
        return@withContext success
    }


    @SuppressLint("MissingPermission")
    fun disconnect() {
        bluetoothGatt?.close()
        bluetoothGatt = null
        notifyCharacteristic = null
        writeCharacteristic = null
        Log.d(TAG, "Disconnected from BLE device")
    }
}
