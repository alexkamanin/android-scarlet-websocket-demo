package io.github.alexkamanin.scarlet.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import io.github.alexkamanin.scarlet.data.exception.WebsocketResponseException
import io.github.alexkamanin.scarlet.domain.entity.Heartbeat
import io.github.alexkamanin.scarlet.domain.entity.Message
import io.github.alexkamanin.scarlet.domain.entity.Query
import io.github.alexkamanin.scarlet.domain.entity.Subscriptions
import io.github.alexkamanin.scarlet.domain.usecase.GetMessagesUseCase
import io.github.alexkamanin.scarlet.domain.usecase.SubscribeUseCase
import io.github.alexkamanin.scarlet.domain.usecase.UnsubscribeUseCase
import io.github.alexkamanin.scarlet.rule.TestCoroutineRule
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class WebsocketViewModelTest {

	@get:Rule
	val taskExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	val testCoroutineRule = TestCoroutineRule()

	private val subscribeUseCase: SubscribeUseCase = mock()
	private val unsubscribeUseCase: UnsubscribeUseCase = mock()
	private val getMessagesUseCase: GetMessagesUseCase = mock()

	private val viewModel = WebsocketViewModel(
		subscribeUseCase,
		unsubscribeUseCase,
		getMessagesUseCase
	)

	@Test
	fun `subscribe EXPECT subscribe called`() {
		val query = Query(
			type = "subscribe",
			productIds = listOf("ETH-USD", "ETH-EUR"),
			channels = listOf("ticker", "heartbeat")
		)
		viewModel.subscribe()

		verify(subscribeUseCase).invoke(query)
	}

	@Test
	fun `subscribe EXPECT subscribed on messages`() {
		viewModel.subscribe()

		verify(getMessagesUseCase).invoke()
	}

	@Test
	fun `unsubscribe EXPECT unsubscribe called`() {
		viewModel.unsubscribe()

		verify(unsubscribeUseCase).invoke()
	}

	@Test
	fun `view model created EXPECT initial state`() = runTest {
		val expectedState = WebsocketState(
			subscriptionEnabled = true,
			messages = emptyMap()
		)

		viewModel.state.test {
			val actualState = expectMostRecentItem()
			assertEquals(expectedState, actualState)
		}
	}

	@Test
	fun `subscribe EXPECT state with subscription disabled`() = runTest {
		val expectedState = WebsocketState(
			subscriptionEnabled = false,
			messages = emptyMap()
		)
		whenever(getMessagesUseCase()).thenReturn(emptyFlow())

		viewModel.subscribe()

		viewModel.state.test {
			val actualState = expectMostRecentItem()
			assertEquals(expectedState, actualState)
		}
	}

	@Test
	fun `unsubscribe EXPECT state with subscription enabled`() = runTest {
		val expectedState = WebsocketState(
			subscriptionEnabled = true,
			messages = emptyMap()
		)

		viewModel.subscribe()
		viewModel.unsubscribe()

		viewModel.state.test {
			val actualState = expectMostRecentItem()
			assertEquals(expectedState, actualState)
		}
	}

	@Test
	fun `receive message EXPECT state with message`() = runTest {
		val message = Heartbeat(12, "ETH-USD", 123456789, "2022-12-12T12:00:23.477349Z")
		val expectedState = WebsocketState(
			subscriptionEnabled = false,
			messages = mapOf("ETH-USD:Heartbeat" to message)
		)
		val messagesFlow = MutableSharedFlow<Message>()
		whenever(getMessagesUseCase()).thenReturn(messagesFlow)

		viewModel.subscribe()

		viewModel.state.test {
			messagesFlow.emit(message)

			val actualState = expectMostRecentItem()
			assertEquals(expectedState, actualState)
		}
	}

	@Test
	fun `receive subscriptions message EXPECT state with message`() = runTest {
		val expectedSubscriptions = Subscriptions(channels = listOf(Subscriptions.Channel("channelName", listOf("ETH-USD"))))
		val messagesFlow = MutableSharedFlow<Message>()
		whenever(getMessagesUseCase()).thenReturn(messagesFlow)

		viewModel.subscribe()

		viewModel.subscriptions.test {
			messagesFlow.emit(expectedSubscriptions)

			val actualSubscriptions = expectMostRecentItem()
			assertEquals(expectedSubscriptions, actualSubscriptions)
		}
	}

	@Test
	fun `receive messages returns error EXPECT subscription enabled and empty messages`() = runTest {
		val expectedState = WebsocketState(
			subscriptionEnabled = true,
			messages = emptyMap()
		)
		val messagesFlow = flow<Message> { throw WebsocketResponseException("message", "reason") }
		whenever(getMessagesUseCase()).thenReturn(messagesFlow)

		viewModel.subscribe()

		viewModel.state.test {
			val actualState = expectMostRecentItem()
			assertEquals(expectedState, actualState)
		}
	}

	@Test
	fun `receive messages returns error EXPECT unsubscribe called`() {
		val messagesFlow = flow<Message> { throw WebsocketResponseException("message", "reason") }
		whenever(getMessagesUseCase()).thenReturn(messagesFlow)

		viewModel.subscribe()

		verify(unsubscribeUseCase).invoke()
	}
}