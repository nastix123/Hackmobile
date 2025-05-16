package com.hackmobile.hackmobile.data.remote

import com.google.gson.annotations.SerializedName
import com.hackmobile.hackmobile.data.remote.model.LoginRequest
import com.hackmobile.hackmobile.data.remote.model.LoginResponse
import com.hackmobile.hackmobile.data.remote.model.RefreshRequest
import com.hackmobile.hackmobile.data.remote.model.RefreshResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationApi {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): RefreshResponse

    @POST("/v1/hotel/rooms")
    suspend fun getRooms(@Body request: RoomRequest): ReserveResponse

    @POST("/v1/hotel/rooms/reserve")
    suspend fun reserve(@Body request: ReserveRequest)

    @GET("/v1/hotel/rooms/{roomId}")
    suspend fun getRoomInfo(
        @Path("roomId") roomId: Long
    ): RoomInfoResponse
}



data class RoomInfoResponse(
    @SerializedName("floor")
    val floor: Int,

    @SerializedName("hotel_id")
    val hotelId: Int,

    @SerializedName("room_number")
    val roomNumber: String,

    @SerializedName("room_type_id")
    val roomTypeId: Int
)


data class RoomRequest(
    @SerializedName("end_date")
    val endDate: String,  // Формат: "dd.MM.yyyy" (29.05.2026)

    @SerializedName("start_date")
    val startDate: String  // Формат: "dd.MM.yyyy" (01.12.2025)
)

data class ReserveResponse(
    @SerializedName("rooms")
    val rooms: List<RoomDto>
)

data class RoomDto(
    @SerializedName("capacity")
    val capacity: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)


data class ReserveRequest(
    @SerializedName("end_date")
    val endDate: String,  // Формат: "dd.MM.yyyy" (29.05.2026)

    @SerializedName("room_type")
    val type: Int,  // Формат: "dd.MM.yyyy" (29.05.2026)

    @SerializedName("start_date")
    val startDate: String  // Формат: "dd.MM.yyyy" (01.12.2025)
)

data class ReservationDto(
    val id: String,
    val user: String,
    val date: String
)
