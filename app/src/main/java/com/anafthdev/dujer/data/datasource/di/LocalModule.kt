package com.anafthdev.dujer.data.datasource.di

import android.content.Context
import com.anafthdev.dujer.data.datasource.local.db.AppDatabase
import com.anafthdev.dujer.data.datasource.local.db.DujerReadDao
import com.anafthdev.dujer.data.datasource.local.db.DujerWriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
	
	@Provides
	@Singleton
	fun provideAppDatabase(
		@ApplicationContext context: Context
	): AppDatabase = AppDatabase.getInstance(context)
	
	@Provides
	@Singleton
	fun provideDujerReadDao(
		appDatabase: AppDatabase
	): DujerReadDao = appDatabase.readDao()
	
	@Provides
	@Singleton
	fun provideDujerWriteDao(
		appDatabase: AppDatabase
	): DujerWriteDao = appDatabase.writeDao()
	
}