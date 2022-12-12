package io.github.alexkamanin.scarlet.data.exception

data class WebsocketResponseException(
	override val message: String,
	val reason: String
) : Exception()