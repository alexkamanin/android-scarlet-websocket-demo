package io.github.alexkamanin.scarlet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexkamanin.scarlet.domain.entity.Message
import io.github.alexkamanin.scarlet.domain.entity.ProductMessage
import io.github.alexkamanin.scarlet.domain.entity.Query
import io.github.alexkamanin.scarlet.domain.entity.Subscriptions
import io.github.alexkamanin.scarlet.domain.usecase.GetMessagesUseCase
import io.github.alexkamanin.scarlet.domain.usecase.SubscribeUseCase
import io.github.alexkamanin.scarlet.domain.usecase.UnsubscribeUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WebsocketViewModel @Inject constructor(
	private val subscribeUseCase: SubscribeUseCase,
	private val unsubscribeUseCase: UnsubscribeUseCase,
	private val getMessagesUseCase: GetMessagesUseCase
) : ViewModel() {

	private val _state = MutableStateFlow<WebsocketState>(computeInitialState())
	val state: StateFlow<WebsocketState> = _state.asStateFlow()

	private val _subscriptions = MutableSharedFlow<Subscriptions>(replay = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
	val subscriptions: SharedFlow<Subscriptions> = _subscriptions.asSharedFlow()

	private val _error = MutableSharedFlow<Throwable>(replay = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
	val error: SharedFlow<Throwable> = _error.asSharedFlow()

	private fun computeInitialState(): WebsocketState =
		WebsocketState(
			subscriptionEnabled = true,
			messages = emptyMap()
		)

	fun subscribe() {
		subscribeUseCase(Query.SAMPLE_SUBSCRIPTION)
		handleSubscribed()

		getMessagesUseCase()
			.catch { throwable -> handleException(throwable) }
			.onEach { message -> handleMessage(message) }
			.launchIn(viewModelScope)
	}

	private fun handleSubscribed() {
		_state.value = _state.value
			.copy(subscriptionEnabled = false)
	}

	private fun handleMessage(message: Message) {
		when (message) {
			is Subscriptions  -> _subscriptions.tryEmit(message)
			is ProductMessage -> updateMessage(message)
		}
	}

	private fun handleException(exception: Throwable) {
		_error.tryEmit(exception)

		unsubscribeUseCase()

		_state.value = _state.value
			.copy(subscriptionEnabled = true, messages = emptyMap())
	}

	private fun updateMessage(message: ProductMessage) {
		val currentState = _state.value

		if (!currentState.subscriptionEnabled) {
			val messages = currentState.messages
				.toMutableMap()
				.apply { put("${message.productId}:${message::class.java.simpleName}", message) }

			_state.value = currentState.copy(messages = messages)
		}
	}

	fun unsubscribe() {
		unsubscribeUseCase()
		handleUnsubscribed()
	}

	private fun handleUnsubscribed() {
		_state.value = _state.value
			.copy(subscriptionEnabled = true, messages = emptyMap())
	}
}