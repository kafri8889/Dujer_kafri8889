package com.anafthdev.dujer.data.datastore.di

import android.content.Context
import com.anafthdev.dujer.data.datastore.AppDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {
	
	@Provides
	fun provideAppDatastore(
		@ApplicationContext context: Context
	): AppDatastore = AppDatastore(context)
}