package com.example.cornache.data.api.response

import com.google.gson.annotations.SerializedName

data class RoomListResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("detail_room")
	val detailRoom: DetailRoom? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

