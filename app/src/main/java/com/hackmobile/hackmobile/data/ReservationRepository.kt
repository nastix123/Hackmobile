package com.hackmobile.hackmobile.data

import com.hackmobile.hackmobile.data.remote.ReservationApi
import com.hackmobile.hackmobile.data.remote.ReservationDto
import com.hackmobile.hackmobile.data.remote.ReserveRequest
import com.hackmobile.hackmobile.data.remote.ReserveResponse
import com.hackmobile.hackmobile.data.remote.RoomDto
import com.hackmobile.hackmobile.data.remote.RoomInfoResponse
import com.hackmobile.hackmobile.data.remote.RoomRequest
import com.hackmobile.hackmobile.data.remote.model.LoginRequest
import com.hackmobile.hackmobile.data.remote.model.LoginResponse
import com.hackmobile.hackmobile.data.remote.model.RefreshRequest
import com.hackmobile.hackmobile.data.remote.model.RefreshResponse
import com.hackmobile.hackmobile.domain.CommonRepository

class ReservationRepositoryImpl(
    private val api: ReservationApi
) : CommonRepository {
    override suspend fun login(login: String, password: String): LoginResponse {
        return api.login(LoginRequest(login, password))
    }

    override suspend fun refreshToken(refreshToken: String): RefreshResponse {
        return api.refresh(RefreshRequest(refreshToken))
    }

    override suspend fun getAvailableRooms(startDate: String, endDate: String): List<RoomDto> {
        return api.getRooms(RoomRequest(startDate, endDate)).rooms
    }

    override suspend fun getRoomInfo(roomId: Long): RoomInfoResponse {
        return api.getRoomInfo(roomId)
    }

    override suspend fun reserveRoom(roomId: Int, startDate: String, endDate: String){
        return api.reserve(ReserveRequest(type = roomId, startDate = startDate, endDate = endDate))
    }

}
