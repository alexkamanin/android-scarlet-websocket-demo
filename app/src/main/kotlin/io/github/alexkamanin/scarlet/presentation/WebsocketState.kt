package io.github.alexkamanin.scarlet.presentation

import io.github.alexkamanin.scarlet.domain.entity.Message

data class WebsocketState(
	val subscriptionEnabled: Boolean,
	val messages: Map<String, Message>,
)