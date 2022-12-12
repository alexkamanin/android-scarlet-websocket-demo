package io.github.alexkamanin.scarlet.data.api

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.github.alexkamanin.scarlet.data.model.MessageModel
import io.github.alexkamanin.scarlet.data.model.QueryModel
import kotlinx.coroutines.channels.ReceiveChannel

interface MessageApi {

	@Send
	fun send(query: QueryModel)

	@Receive
	fun getEvents(): ReceiveChannel<WebSocket.Event>

	@Receive
	fun getChannel(): ReceiveChannel<MessageModel>
}