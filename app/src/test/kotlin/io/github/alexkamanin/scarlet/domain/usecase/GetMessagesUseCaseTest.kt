package io.github.alexkamanin.scarlet.domain.usecase

import io.github.alexkamanin.scarlet.domain.repository.MessageRepository
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class GetMessagesUseCaseTest {

	private val repository: MessageRepository = mock()
	private val useCase = GetMessagesUseCase(repository)

	@Test
	fun `invoke EXPECT get flow`() {
		useCase()

		verify(repository).getFlow()
	}
}