package io.github.alexkamanin.scarlet.data.converter

import io.github.alexkamanin.scarlet.data.model.*
import io.github.alexkamanin.scarlet.domain.entity.*
import javax.inject.Inject

class MessageConverter @Inject constructor(
	private val dateConverter: DateConverter
) {

	fun convert(model: MessageModel): Message =
		when (model) {
			is SubscriptionsModel -> model.toEntity()
			is SnapshotModel      -> model.toEntity()
			is TickerModel        -> model.toEntity()
			is HeartbeatModel     -> model.toEntity()
			else                  -> throw IllegalStateException("Unknown message model: ${model::class.java}")
		}

	private fun SubscriptionsModel.toEntity(): Subscriptions =
		Subscriptions(
			channels = channels.map { channel -> channel.toEntity() }
		)

	private fun SubscriptionsModel.ChannelModel.toEntity(): Subscriptions.Channel =
		Subscriptions.Channel(
			name = name,
			productIds = productIds
		)

	private fun SnapshotModel.toEntity(): Snapshot =
		Snapshot(
			productId = productId,
			asks = asks
		)

	private fun TickerModel.toEntity(): Ticker =
		Ticker(
			sequence = sequence,
			productId = productId,
			price = price,
			open24h = open24h,
			volume24h = volume24h,
			low24h = low24h,
			high24h = high24h,
			volume30d = volume30d,
			bestBid = bestBid,
			bestBidSize = bestBidSize,
			bestAsk = bestAsk,
			bestAskSize = bestAskSize,
			side = side,
			time = dateConverter.convert(time),
			tradeId = tradeId,
			lastSize = lastSize
		)

	private fun HeartbeatModel.toEntity(): Heartbeat =
		Heartbeat(
			lastTradeId = lastTradeId,
			productId = productId,
			sequence = sequence,
			time = dateConverter.convert(time)
		)
}