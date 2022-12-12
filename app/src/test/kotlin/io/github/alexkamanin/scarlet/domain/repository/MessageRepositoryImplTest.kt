package io.github.alexkamanin.scarlet.domain.repository

import io.github.alexkamanin.scarlet.data.converter.MessageConverter
import io.github.alexkamanin.scarlet.data.converter.QueryConverter
import io.github.alexkamanin.scarlet.data.datasource.MessageDataSource
import io.github.alexkamanin.scarlet.data.model.QueryModel
import io.github.alexkamanin.scarlet.data.repository.MessageRepositoryImpl
import io.github.alexkamanin.scarlet.domain.entity.Query
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class MessageRepositoryImplTest {

	private val dataSource: MessageDataSource = mock()
	private val queryConverter = QueryConverter()
	private val messageConverter: MessageConverter = mock()
	private val repository: MessageRepository = MessageRepositoryImpl(dataSource, queryConverter, messageConverter)

	private val query = Query(
		type = "subscribe",
		productIds = listOf("ETH-USD", "ETH-EUR"),
		channels = listOf("ticker", "heartbeat")
	)
	private val queryModel = QueryModel(
		type = "subscribe",
		productIds = listOf("ETH-USD", "ETH-EUR"),
		channels = listOf("ticker", "heartbeat")
	)

	@Test
	fun `subscribe EXPECT data source subscribed`() {
		repository.subscribe(query)

		verify(dataSource).subscribe(queryModel)
	}

	@Test
	fun `unsubscribe EXPECT data source unsubscribed`() {
		repository.subscribe(query)
		repository.unsubscribe()

		verify(dataSource).unsubscribe()
	}
}