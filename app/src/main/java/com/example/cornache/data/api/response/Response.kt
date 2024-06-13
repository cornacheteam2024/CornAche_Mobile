package com.example.cornache.data.api.response

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("history")
	val history: List<HistoryItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class HistoryItem(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("prediction")
	val prediction: Prediction? = null
)
