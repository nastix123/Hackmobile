package com.hackmobile.hackmobile.domain

import com.hackmobile.hackmobile.data.remote.ReservationDto
import com.hackmobile.hackmobile.data.remote.ReserveResponse
import com.hackmobile.hackmobile.data.remote.RoomDto
import com.hackmobile.hackmobile.data.remote.RoomInfoResponse
import com.hackmobile.hackmobile.data.remote.model.LoginResponse
import com.hackmobile.hackmobile.data.remote.model.RefreshResponse
import java.io.IOException

interface CommonRepository {
    // Authentication
    @Throws(IOException::class, ApiException::class)
    suspend fun login(login: String, password: String): LoginResponse

    @Throws(IOException::class, ApiException::class)
    suspend fun refreshToken(refreshToken: String): RefreshResponse

    // Rooms
    @Throws(IOException::class, ApiException::class)
    suspend fun getAvailableRooms(startDate: String, endDate: String): List<RoomDto>

    @Throws(IOException::class, ApiException::class)
    suspend fun getRoomInfo(roomId: Long): RoomInfoResponse

    // Reservations
    @Throws(IOException::class, ApiException::class)
    suspend fun reserveRoom(roomId: Int, startDate: String, endDate: String)

}


class ApiException(message: String) : Exception(message)