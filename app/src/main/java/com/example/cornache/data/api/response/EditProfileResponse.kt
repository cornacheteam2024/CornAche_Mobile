package com.example.cornache.data.api.response

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)
