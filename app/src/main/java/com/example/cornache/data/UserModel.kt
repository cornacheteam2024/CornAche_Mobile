package com.example.cornache.data

data class UserModel (
    val userId:String,
    val token:String,
    val isLogin:Boolean = false
)