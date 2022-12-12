package io.github.alexkamanin.scarlet.data.converter

import io.github.alexkamanin.scarlet.data.model.QueryModel
import io.github.alexkamanin.scarlet.domain.entity.Query
import javax.inject.Inject

class QueryConverter @Inject constructor() {

	fun revert(query: Query): QueryModel =
		QueryModel(
			type = query.type,
			productIds = query.productIds,
			channels = query.channels,
		)
}