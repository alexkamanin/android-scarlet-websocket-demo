package io.github.alexkamanin.scarlet.domain.usecase

import io.github.alexkamanin.scarlet.domain.entity.Query
import io.github.alexkamanin.scarlet.domain.repository.MessageRepository
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SubscribeUseCaseTest {

	private val repository: MessageRepository = mock()
	private val useCase = SubscribeUseCase(repository)

	@Test
	fun `invoke EXPECT subscribe`() {
		val query = Query(
			type = "subscribe",
			productIds = listOf("ETH-USD", "ETH-EUR"),
			channels = listOf("ticker", "heartbeat")
		)
		useCase(query)

		verify(repository).subscribe(query)
	}
}