package com.hackmobile.hackmobile.data.remote.model

import com.google.gson.annotations.SerializedName

data class RefreshRequest(
    @SerializedName("refresh_token")
    val refreshToken: String,
)

data class RefreshResponse(
    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("access_token")
    val accessToken: String,
)