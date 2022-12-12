package io.github.alexkamanin.scarlet.data.adapter

import android.util.Log
import com.tinder.scarlet.Message
import com.tinder.scarlet.MessageAdapter
import io.github.alexkamanin.scarlet.BuildConfig
import java.lang.reflect.Type

fun interface MessageAdapterFactory {

	fun create(factory: MessageAdapter.Factory): MessageAdapter.Factory
}

class MessageAdapterFactoryImpl<T>(
	private val messageAdapter: MessageAdapter<T>
) : MessageAdapter<T> {

	private companion object {

		const val TAG = "scarlet.Websocket"
	}

	override fun fromMessage(message: Message): T {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "<-- RECEIVE ${message.asString()}")
		}

		return messageAdapter.fromMessage(message)
	}

	override fun toMessage(data: T): Message =
		messageAdapter
			.toMessage(data)
			.also { message ->
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "--> SEND ${message.asString()}")
				}
			}

	private fun Message.asString(): String =
		when (this) {
			is Message.Text  -> value
			is Message.Bytes -> String(value)
		}

	class Factory(private val factory: MessageAdapter.Factory) : MessageAdapter.Factory {

		override fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*> =
			MessageAdapterFactoryImpl(factory.create(type, annotations))
	}
}