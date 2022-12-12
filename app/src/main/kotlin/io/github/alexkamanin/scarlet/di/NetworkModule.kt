package io.github.alexkamanin.scarlet.di

import android.app.Application
import android.content.Context
import com.tinder.scarlet.MessageAdapter
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.alexkamanin.scarlet.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Provides
	@Singleton
	fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
		HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

	@Provides
	@Singleton
	fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
		OkHttpClient
			.Builder()
			.addInterceptor(loggingInterceptor)
			.build()

	@Provides
	@Singleton
	fun provideScarlet(
		okHttpClient: OkHttpClient,
		messageAdapterFactory: MessageAdapter.Factory,
		application: Application,
		@ApplicationContext context: Context
	): Scarlet =
		Scarlet.Builder()
			.webSocketFactory(okHttpClient.newWebSocketFactory(context.getString(R.string.websocket_url)))
			.addMessageAdapterFactory(messageAdapterFactory)
			.addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
			.lifecycle(AndroidLifecycle.ofApplicationForeground(application))
			.build()
}