package com.example.hotelhackapp.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Immutable
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackmobile.hackmobile.data.remote.RoomDto
import com.hackmobile.hackmobile.data.remote.RoomInfoResponse
import com.hackmobile.hackmobile.data.remote.model.LoginResponse
import com.hackmobile.hackmobile.data.remote.model.RefreshResponse
import com.hackmobile.hackmobile.domain.BluetoothManager
import com.hackmobile.hackmobile.domain.BluetoothPermissionManager
import com.hackmobile.hackmobile.domain.CommonRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainListViewModel(
    private val repository: CommonRepository,
    private val bluetoothManager: BluetoothManager,
    private val context: Context
    ) : ViewModel() {

    init {
        viewModelScope.launch {
            bluetoothManager.responses.collect { response ->
                // Обработка ответа, например, обновление состояния двери по ответу
                if (response.state.doorLockValue == 0) {
                    val success = true
                    _commandResult.value = success
                    _doorState.value = success
                    // При успешном результате можно обновить _doorState, если нужно
                } else {
                    _commandResult.value = false
                    _doorState.value = false
                }
            }
        }
    }

    private val _state = MutableStateFlow<MainListViewState>(MainListViewState.Idle)
    val state: StateFlow<MainListViewState> = _state.asStateFlow()

    private val _doorState = MutableStateFlow(false)
    val doorState: StateFlow<Boolean> = _doorState.asStateFlow()

    private val _commandResult = MutableStateFlow<Boolean?>(null)
    val commandResult: StateFlow<Boolean?> = _commandResult.asStateFlow()

    private val _permissionRequired = MutableStateFlow(false)
    val permissionRequired: StateFlow<Boolean> = _permissionRequired.asStateFlow()



    @SuppressLint("MissingPermission")
    fun toggleDoor(open: Boolean) {
        viewModelScope.launch {

            val success = bluetoothManager.sendDoorCommand(open)
            if (success) _doorState.value = open
            _commandResult.value = success
        }
    }


    sealed class UiState {
        object Idle : UiState()
        object Connecting : UiState()
        object Connected : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _connectionState = MutableStateFlow<UiState>(UiState.Idle)
    val connectionState: StateFlow<UiState> = _connectionState.asStateFlow()

    private val _controllerResponse = MutableSharedFlow<Test.ControllerResponse>(extraBufferCapacity = 5)
    val controllerResponse: SharedFlow<Test.ControllerResponse> = _controllerResponse.asSharedFlow()


    @SuppressLint("MissingPermission")
    fun connectToDevice(deviceName: String, token: String) {
        viewModelScope.launch {
            _connectionState.value = UiState.Connecting
            val success = bluetoothManager.connect("ROOM_38", "tZAZwckrp2ZH4fCM")
            if (success) {
                _connectionState.value = UiState.Connected
            } else {
                _connectionState.value = UiState.Error("Failed to connect")
            }
        }
    }

    fun disconnect() {
        bluetoothManager.disconnect()
        _connectionState.value = UiState.Idle
    }

    @SuppressLint("MissingPermission")
    fun sendDoorCommand(open: Boolean) {
        viewModelScope.launch {
            val success = bluetoothManager.sendDoorCommand(open)
            if (!success) {
                // Можно обновить состояние ошибки или как-то уведомить UI
                Log.e("BluetoothViewModel", "Failed to send door command")
            }
        }
    }






//    fun dispatch(action: MainListAction) {
//        viewModelScope.launch {
//            _state.value = MainListViewState.Loading
//
//            when (action) {
//                is MainListAction.Login -> {
//                    val response = repository.login(action.login, action.password)
//                    _state.value = MainListViewState.LoginSuccess(response)
//                }
//                is MainListAction.RefreshToken -> {
//                    val response = repository.refreshToken(action.refreshToken)
//                    _state.value = MainListViewState.TokenRefreshed(response)
//                }
//                is MainListAction.LoadAvailableRooms -> {
//                    val rooms = repository.getAvailableRooms(action.startDate, action.endDate)
//                    _state.value = MainListViewState.RoomsLoaded(rooms)
//                }
//                is MainListAction.LoadRoomInfo -> {
//                    val roomInfo = repository.getRoomInfo(action.roomId)
//                    _state.value = MainListViewState.RoomInfoLoaded(roomInfo)
//                }
//                is MainListAction.MakeReservation -> {
//                    repository.reserveRoom(action.roomId, action.startDate, action.endDate)
//                    _state.value = MainListViewState.ReservationSuccess
//                }
//            }
//
//        }
//    }
}

sealed class MainListViewState {
    data object Idle : MainListViewState()
    data object Loading : MainListViewState()
    data class LoginSuccess(val response: LoginResponse) : MainListViewState()
    data class TokenRefreshed(val response: RefreshResponse) : MainListViewState()
    data class RoomsLoaded(val rooms: List<RoomDto>) : MainListViewState()
    data class RoomInfoLoaded(val roomInfo: RoomInfoResponse) : MainListViewState()
    data object ReservationSuccess : MainListViewState()
    data class Error(val message: String) : MainListViewState()
}
