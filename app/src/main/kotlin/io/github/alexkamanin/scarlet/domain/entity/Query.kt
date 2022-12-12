package io.github.alexkamanin.scarlet.domain.entity

data class Query(
	val type: String,
	val productIds: List<String>,
	val channels: List<String>,
) {

	companion object {

		val SAMPLE_SUBSCRIPTION = Query(
			type = "subscribe",
			productIds = listOf("ETH-USD", "ETH-EUR"),
			channels = listOf("ticker", "heartbeat")
		)
	}
}