package io.github.alexkamanin.scarlet.data.datasource

import com.tinder.scarlet.WebSocket
import io.github.alexkamanin.scarlet.data.api.MessageApi
import io.github.alexkamanin.scarlet.data.exception.WebsocketResponseException
import io.github.alexkamanin.scarlet.data.model.ErrorModel
import io.github.alexkamanin.scarlet.data.model.MessageModel
import io.github.alexkamanin.scarlet.data.model.QueryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

interface MessageDataSource {

	fun subscribe(query: QueryModel)

	fun unsubscribe()

	fun getFlow(): Flow<MessageModel>
}

class MessageDataSourceImpl @Inject constructor(
	private val api: MessageApi
) : MessageDataSource {

	private var activeQuery: QueryModel? = null

	override fun subscribe(query: QueryModel) {
		activeQuery = query

		api.send(requireNotNull(activeQuery))
	}

	override fun unsubscribe() {
		activeQuery
			?.copy(type = "unsubscribe")
			?.let(api::send)

		activeQuery = null
	}

	override fun getFlow(): Flow<MessageModel> =
		combine(api.getChannel().receiveAsFlow(), api.getEvents().receiveAsFlow()) { message, event ->
			when {
				event is WebSocket.Event.OnConnectionFailed -> throw event.throwable
				message is ErrorModel                       -> throw WebsocketResponseException(message = message.message, reason = message.reason)
				else                                        -> return@combine message
			}
		}.flowOn(Dispatchers.IO)
}