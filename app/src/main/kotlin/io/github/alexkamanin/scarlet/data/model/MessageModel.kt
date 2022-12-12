package io.github.alexkamanin.scarlet.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

const val MESSAGE_KEY = "type"

const val SUBSCRIPTIONS_MESSAGE = "subscriptions"
const val SNAPSHOT_MESSAGE = "snapshot"
const val TICKER_MESSAGE = "ticker"
const val HEARTBEAT_MESSAGE = "heartbeat"
const val ERROR_MESSAGE = "error"

sealed interface MessageModel {

	val type: String
}

@JsonClass(generateAdapter = true)
data class SubscriptionsModel(
	override val type: String,
	val channels: List<ChannelModel>
) : MessageModel {

	data class ChannelModel(
		val name: String,
		@Json(name = "product_ids")
		val productIds: List<String>
	)
}

@JsonClass(generateAdapter = true)
data class SnapshotModel(
	override val type: String,
	@Json(name = "product_id")
	val productId: String,
	val asks: List<List<String>>
) : MessageModel

@JsonClass(generateAdapter = true)
data class TickerModel(
	override val type: String,
	val sequence: Long,
	@Json(name = "product_id")
	val productId: String,
	val price: String,
	@Json(name = "open_24h")
	val open24h: String,
	@Json(name = "volume_24h")
	val volume24h: String,
	@Json(name = "low_24h")
	val low24h: String,
	@Json(name = "high_24h")
	val high24h: String,
	@Json(name = "volume_30d")
	val volume30d: String,
	@Json(name = "best_bid")
	val bestBid: String,
	@Json(name = "best_bid_size")
	val bestBidSize: String,
	@Json(name = "best_ask")
	val bestAsk: String,
	@Json(name = "best_ask_size")
	val bestAskSize: String,
	val side: String,
	val time: String,
	@Json(name = "trade_id")
	val tradeId: String,
	@Json(name = "last_size")
	val lastSize: String
) : MessageModel

@JsonClass(generateAdapter = true)
data class HeartbeatModel(
	override val type: String,
	@Json(name = "last_trade_id")
	val lastTradeId: Long,
	@Json(name = "product_id")
	val productId: String,
	val sequence: Long,
	val time: String
) : MessageModel

@JsonClass(generateAdapter = true)
data class ErrorModel(
	override val type: String,
	val message: String,
	val reason: String
) : MessageModel