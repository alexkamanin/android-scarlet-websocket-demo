package io.github.alexkamanin.scarlet.domain.usecase

import io.github.alexkamanin.scarlet.domain.repository.MessageRepository
import javax.inject.Inject

class UnsubscribeUseCase @Inject constructor(
	private val repository: MessageRepository
) {

	operator fun invoke() {
		repository.unsubscribe()
	}
}