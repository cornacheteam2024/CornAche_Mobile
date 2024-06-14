package com.example.cornache.data.api.response

import com.google.gson.annotations.SerializedName

data class RoomResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("room")
	val room: Room? = null
)

data class Room(

	@field:SerializedName("detail_room")
	val detailRoom: DetailRoom? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class DetailRoom(

	@field:SerializedName("room_id")
	val roomId: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("update_at")
	val updateAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null
)
