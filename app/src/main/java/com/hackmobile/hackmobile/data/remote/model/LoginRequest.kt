package com.hackmobile.hackmobile.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("login")
    val login: String,

    @SerializedName("password")
    val password: String,
)