package io.github.alexkamanin.scarlet.domain.usecase

import io.github.alexkamanin.scarlet.domain.repository.MessageRepository
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UnsubscribeUseCaseTest {

	private val repository: MessageRepository = mock()
	private val useCase = UnsubscribeUseCase(repository)

	@Test
	fun `invoke EXPECT unsubscribe`() {
		useCase()

		verify(repository).unsubscribe()
	}
}