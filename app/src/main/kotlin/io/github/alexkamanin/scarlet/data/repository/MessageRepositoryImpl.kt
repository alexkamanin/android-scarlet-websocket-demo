package io.github.alexkamanin.scarlet.data.repository

import io.github.alexkamanin.scarlet.data.converter.MessageConverter
import io.github.alexkamanin.scarlet.data.converter.QueryConverter
import io.github.alexkamanin.scarlet.data.datasource.MessageDataSource
import io.github.alexkamanin.scarlet.domain.entity.Message
import io.github.alexkamanin.scarlet.domain.entity.Query
import io.github.alexkamanin.scarlet.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
	private val dataSource: MessageDataSource,
	private val queryConverter: QueryConverter,
	private val messageConverter: MessageConverter,
) : MessageRepository {

	override fun subscribe(query: Query) {
		queryConverter.revert(query)
			.also(dataSource::subscribe)
	}

	override fun unsubscribe() {
		dataSource.unsubscribe()
	}

	override fun getFlow(): Flow<Message> =
		dataSource.getFlow()
			.map(messageConverter::convert)
}