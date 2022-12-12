package io.github.alexkamanin.scarlet.domain.usecase

import io.github.alexkamanin.scarlet.domain.entity.Query
import io.github.alexkamanin.scarlet.domain.repository.MessageRepository
import javax.inject.Inject

class SubscribeUseCase @Inject constructor(
	private val repository: MessageRepository
) {

	operator fun invoke(query: Query) {
		repository.subscribe(query)
	}
}