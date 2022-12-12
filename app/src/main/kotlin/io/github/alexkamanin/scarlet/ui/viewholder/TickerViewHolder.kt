package io.github.alexkamanin.scarlet.ui.viewholder

import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.alexkamanin.scarlet.R
import io.github.alexkamanin.scarlet.databinding.ItemTickerBinding
import io.github.alexkamanin.scarlet.domain.entity.Ticker

class TickerViewHolder(parent: ViewGroup) : MessageViewHolder<Ticker>(parent, R.layout.item_ticker) {

	private val viewBinding by viewBinding(ItemTickerBinding::bind)

	override fun bind(message: Ticker) {
		viewBinding.name.text = message.productId
		viewBinding.time.text = message.time
		viewBinding.price.text = itemView.context.getString(R.string.price_format, message.price)
	}
}