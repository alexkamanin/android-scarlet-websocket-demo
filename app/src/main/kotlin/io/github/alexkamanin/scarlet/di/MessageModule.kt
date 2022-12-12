package io.github.alexkamanin.scarlet.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.MessageAdapter
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.alexkamanin.scarlet.data.adapter.MessageAdapterFactory
import io.github.alexkamanin.scarlet.data.adapter.MessageAdapterFactoryImpl
import io.github.alexkamanin.scarlet.data.api.MessageApi
import io.github.alexkamanin.scarlet.data.datasource.MessageDataSource
import io.github.alexkamanin.scarlet.data.datasource.MessageDataSourceImpl
import io.github.alexkamanin.scarlet.data.model.*
import io.github.alexkamanin.scarlet.data.repository.MessageRepositoryImpl
import io.github.alexkamanin.scarlet.domain.repository.MessageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MessageModule {

	companion object {

		@Provides
		@Singleton
		fun provideMessageApi(scarlet: Scarlet): MessageApi =
			scarlet.create()

		@Provides
		@Singleton
		fun providePolymorphicJsonMessageAdapterFactory(): PolymorphicJsonAdapterFactory<MessageModel> =
			PolymorphicJsonAdapterFactory.of(MessageModel::class.java, MESSAGE_KEY)
				.withSubtype(SnapshotModel::class.java, SNAPSHOT_MESSAGE)
				.withSubtype(HeartbeatModel::class.java, HEARTBEAT_MESSAGE)
				.withSubtype(TickerModel::class.java, TICKER_MESSAGE)
				.withSubtype(SubscriptionsModel::class.java, SUBSCRIPTIONS_MESSAGE)
				.withSubtype(ErrorModel::class.java, ERROR_MESSAGE)

		@Provides
		@Singleton
		fun provideMoshi(polymorphicJsonMessageAdapterFactory: PolymorphicJsonAdapterFactory<MessageModel>): Moshi =
			Moshi.Builder()
				.add(polymorphicJsonMessageAdapterFactory)
				.add(KotlinJsonAdapterFactory())
				.build()

		@Provides
		@Singleton
		fun provideMessageAdapter(messageAdapterFactory: MessageAdapterFactory, moshi: Moshi): MessageAdapter.Factory {
			val moshiMessageAdapter = MoshiMessageAdapter.Factory(moshi)
			return messageAdapterFactory.create(moshiMessageAdapter)
		}

		@Provides
		@Singleton
		fun provideMessageAdapterFactory(): MessageAdapterFactory =
			MessageAdapterFactory { factory -> MessageAdapterFactoryImpl.Factory(factory) }
	}

	@Binds
	@Singleton
	fun bindMessageDataSource(impl: MessageDataSourceImpl): MessageDataSource

	@Binds
	@Singleton
	fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository
}