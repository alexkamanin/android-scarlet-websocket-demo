package io.github.alexkamanin.scarlet.domain.repository

import io.github.alexkamanin.scarlet.domain.entity.Message
import io.github.alexkamanin.scarlet.domain.entity.Query
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

	fun subscribe(query: Query)

	fun unsubscribe()

	fun getFlow(): Flow<Message>
}