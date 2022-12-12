package io.github.alexkamanin.scarlet.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import io.github.alexkamanin.scarlet.domain.entity.Message

abstract class MessageViewHolder<out T : Message>(parent: ViewGroup, @LayoutRes id: Int) : RecyclerView.ViewHolder(parent.inflate(id)) {

	abstract fun bind(message: @UnsafeVariance T)
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View =
	LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)