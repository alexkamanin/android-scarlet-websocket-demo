package io.github.alexkamanin.scarlet.ui.viewholder

import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.alexkamanin.scarlet.R
import io.github.alexkamanin.scarlet.databinding.ItemHeartbeatBinding
import io.github.alexkamanin.scarlet.domain.entity.Heartbeat

class HeartbeatViewHolder(parent: ViewGroup) : MessageViewHolder<Heartbeat>(parent, R.layout.item_heartbeat) {

	private val viewBinding by viewBinding(ItemHeartbeatBinding::bind)

	override fun bind(message: Heartbeat) {
		viewBinding.name.text = message.productId
		viewBinding.time.text = message.time
		viewBinding.sequence.text = itemView.context.getString(R.string.sequence_format, message.sequence)
	}
}