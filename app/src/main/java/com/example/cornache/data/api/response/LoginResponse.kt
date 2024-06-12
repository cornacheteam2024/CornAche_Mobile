package com.example.cornache.data.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("user")
    val user: User? = null,
)

data class User(
    @field:SerializedName("avatar_img")
    val avatarImg: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)

data class LoginResult(
    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,
)
