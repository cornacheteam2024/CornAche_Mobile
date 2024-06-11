package com.example.cornache.data.api

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

	@field:SerializedName("history")
	val history: History? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Prediction(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("confidence_score")
	val confidenceScore: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null
)

data class History(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("prediction")
	val prediction: Prediction? = null
)
