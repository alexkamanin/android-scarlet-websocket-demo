package io.github.alexkamanin.scarlet.domain.entity

sealed interface Message

sealed interface ProductMessage : Message {

	val productId: String
}

data class Subscriptions(
	val channels: List<Channel>
) : Message {

	data class Channel(
		val name: String,
		val productIds: List<String>
	)
}

data class Snapshot(
	override val productId: String,
	val asks: List<List<String>>
) : ProductMessage

data class Ticker(
	val sequence: Long,
	override val productId: String,
	val price: String,
	val open24h: String,
	val volume24h: String,
	val low24h: String,
	val high24h: String,
	val volume30d: String,
	val bestBid: String,
	val bestBidSize: String,
	val bestAsk: String,
	val bestAskSize: String,
	val side: String,
	val time: String,
	val tradeId: String,
	val lastSize: String
) : ProductMessage

data class Heartbeat(
	val lastTradeId: Long,
	override val productId: String,
	val sequence: Long,
	val time: String
) : ProductMessage