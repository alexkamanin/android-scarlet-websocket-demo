package io.github.alexkamanin.scarlet.ui.viewholder

import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.alexkamanin.scarlet.R
import io.github.alexkamanin.scarlet.databinding.ItemSnapshotBinding
import io.github.alexkamanin.scarlet.domain.entity.Snapshot

class SnapshotViewHolder(parent: ViewGroup) : MessageViewHolder<Snapshot>(parent, R.layout.item_snapshot) {

	private val viewBinding by viewBinding(ItemSnapshotBinding::bind)

	override fun bind(message: Snapshot) {
		viewBinding.name.text = message.productId
	}
}