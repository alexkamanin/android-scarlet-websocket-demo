package io.github.alexkamanin.scarlet.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.alexkamanin.scarlet.R
import io.github.alexkamanin.scarlet.domain.entity.Heartbeat
import io.github.alexkamanin.scarlet.domain.entity.Message
import io.github.alexkamanin.scarlet.domain.entity.Snapshot
import io.github.alexkamanin.scarlet.domain.entity.Ticker
import io.github.alexkamanin.scarlet.ui.viewholder.HeartbeatViewHolder
import io.github.alexkamanin.scarlet.ui.viewholder.MessageViewHolder
import io.github.alexkamanin.scarlet.ui.viewholder.SnapshotViewHolder
import io.github.alexkamanin.scarlet.ui.viewholder.TickerViewHolder

class MessageAdapter : RecyclerView.Adapter<MessageViewHolder<Message>>() {

	var items: List<Message> = emptyList()
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder<Message> =
		when (viewType) {
			R.layout.item_ticker    -> TickerViewHolder(parent)
			R.layout.item_heartbeat -> HeartbeatViewHolder(parent)
			R.layout.item_snapshot  -> SnapshotViewHolder(parent)
			else                    -> throw Exception()
		}

	override fun onBindViewHolder(holder: MessageViewHolder<Message>, position: Int) {
		holder.bind(items[position])
	}

	override fun getItemCount(): Int = items.size

	override fun getItemViewType(position: Int): Int =
		when (items[position]) {
			is Ticker    -> R.layout.item_ticker
			is Heartbeat -> R.layout.item_heartbeat
			is Snapshot  -> R.layout.item_snapshot
			else         -> throw Exception()
		}
}