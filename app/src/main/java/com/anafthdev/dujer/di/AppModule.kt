package com.anafthdev.dujer.di

import android.content.Context
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.data.repository.app.AppRepository
import com.anafthdev.dujer.data.datastore.di.DatastoreModule
import com.anafthdev.dujer.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	
	@Provides
	fun provideAppRepository(
		@ApplicationContext context: Context
	): IAppRepository = AppRepository(
		provideAppDatabase(context),
		DatastoreModule.provideAppDatastore(context)
	)
	
	@Provides
	fun provideAppDatabase(
		@ApplicationContext context: Context
	): AppDatabase = AppDatabase.getInstance(context)
}