package com.example.cornache.data.api.response

import com.google.gson.annotations.SerializedName

data class AllCommentResponse(

	@field:SerializedName("chats")
	val chats: List<ChatsItem> = emptyList(),

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Profile(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("avatar")
	val avatar: Any? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class ChatsItem(

	@field:SerializedName("room_id")
	val roomId: String? = null,

	@field:SerializedName("profile")
	val profile: Profile? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("chat_id")
	val chatId: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)
