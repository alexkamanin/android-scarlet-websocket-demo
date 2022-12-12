package io.github.alexkamanin.scarlet.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexkamanin.scarlet.R
import io.github.alexkamanin.scarlet.data.exception.WebsocketResponseException
import io.github.alexkamanin.scarlet.databinding.ActivityWebsocketBinding
import io.github.alexkamanin.scarlet.domain.entity.Subscriptions
import io.github.alexkamanin.scarlet.presentation.WebsocketState
import io.github.alexkamanin.scarlet.presentation.WebsocketViewModel
import io.github.alexkamanin.scarlet.ui.adapter.MessageAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class WebsocketActivity : AppCompatActivity(R.layout.activity_websocket) {

	private val viewBinding: ActivityWebsocketBinding by viewBinding()
	private val viewModel: WebsocketViewModel by viewModels()

	private var adapter: MessageAdapter? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		adapter = MessageAdapter()
		viewBinding.recyclerView.adapter = adapter

		viewBinding.subscribeButton.setOnClickListener { viewModel.subscribe() }
		viewBinding.unsubscribeButton.setOnClickListener { viewModel.unsubscribe() }

		viewModel.state.onEach(::applyState).launchIn(lifecycleScope)
		viewModel.subscriptions.onEach(::showSubscriptions).launchIn(lifecycleScope)
		viewModel.error.onEach(::showErrorDialog).launchIn(lifecycleScope)
	}

	private fun applyState(state: WebsocketState) {
		viewBinding.subscribeButton.isEnabled = state.subscriptionEnabled
		viewBinding.unsubscribeButton.isEnabled = !state.subscriptionEnabled

		adapter?.items = state.messages.values.toList()
	}

	private fun showSubscriptions(subscriptions: Subscriptions) {
		val description = getString(R.string.subscription_notification, subscriptions.channels.isNotEmpty())
		Toast.makeText(this, description, Toast.LENGTH_SHORT).show()
	}

	private fun showErrorDialog(exception: Throwable) {
		if (exception is WebsocketResponseException) {
			AlertDialog.Builder(this)
				.setTitle(exception.message)
				.setMessage(exception.reason)
				.setPositiveButton(R.string.error_positive_dialog, null)
				.show()

		} else {
			AlertDialog.Builder(this)
				.setTitle(R.string.error_title)
				.setMessage(exception.message ?: exception.stackTraceToString())
				.setPositiveButton(R.string.error_positive_dialog, null)
				.show()
		}
	}

	override fun onDestroy() {
		viewBinding.recyclerView.adapter = null
		adapter = null
		super.onDestroy()
	}
}