package io.github.alexkamanin.scarlet.domain.usecase

import io.github.alexkamanin.scarlet.domain.entity.Message
import io.github.alexkamanin.scarlet.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
	private val repository: MessageRepository
) {

	operator fun invoke(): Flow<Message> =
		repository.getFlow()
}