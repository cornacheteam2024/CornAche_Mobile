package com.example.cornache.data.api.response

import com.google.gson.annotations.SerializedName

data class EditRoomResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("room")
	val room: Room? = null
)
