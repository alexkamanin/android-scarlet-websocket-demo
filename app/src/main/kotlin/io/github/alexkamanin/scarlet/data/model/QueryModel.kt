package io.github.alexkamanin.scarlet.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QueryModel(
	val type: String,
	@Json(name = "product_ids")
	val productIds: List<String>,
	val channels: List<String>,
)