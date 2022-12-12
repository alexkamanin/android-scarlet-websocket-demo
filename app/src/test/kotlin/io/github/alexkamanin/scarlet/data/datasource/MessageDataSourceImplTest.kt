package io.github.alexkamanin.scarlet.data.datasource

import io.github.alexkamanin.scarlet.data.api.MessageApi
import io.github.alexkamanin.scarlet.data.model.QueryModel
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class MessageDataSourceImplTest {

	private val api: MessageApi = mock()
	private val dataSource: MessageDataSource = MessageDataSourceImpl(api)

	@Test
	fun `subscribe EXPECT send subscription query`() {
		val query = QueryModel(
			type = "subscribe",
			productIds = listOf("ETH-USD", "ETH-EUR"),
			channels = listOf("ticker", "heartbeat")
		)
		dataSource.subscribe(query)

		verify(api).send(query)
	}

	@Test
	fun `get flow EXPECT get channel`() {
		dataSource.getFlow()

		verify(api).getChannel()
	}

	@Test
	fun `unsubscribe EXPECT send unsubscription query`() {
		val query = QueryModel(
			type = "subscribe",
			productIds = listOf("ETH-USD", "ETH-EUR"),
			channels = listOf("ticker", "heartbeat")
		)
		dataSource.subscribe(query)
		dataSource.unsubscribe()

		verify(api).send(query.copy(type = "unsubscribe"))
	}
}