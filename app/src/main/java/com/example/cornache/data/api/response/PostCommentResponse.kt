package com.example.cornache.data.api.response

import com.google.gson.annotations.SerializedName

data class PostCommentResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null
)
