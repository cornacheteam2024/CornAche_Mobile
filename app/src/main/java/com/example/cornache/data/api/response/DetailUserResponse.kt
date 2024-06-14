package com.example.cornache.data.api.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("user")
	val user: User? = null
)
