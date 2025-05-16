package com.hackmobile.hackmobile.data.remote.model
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("user")
    val user: UserDto
)

data class UserDto(
    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("login")
    val login: String,

    @SerializedName("user_type")
    val userType: String
)