package com.anafthdev.dujer.data.datasource.di

import com.anafthdev.dujer.data.datasource.local.LocalDatasource
import com.anafthdev.dujer.data.datasource.local.db.DujerReadDao
import com.anafthdev.dujer.data.datasource.local.db.DujerWriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatasourceModule {
	
	@Provides
	@Singleton
	fun provideLocalDatasource(
		readDao: DujerReadDao,
		writeDao: DujerWriteDao
	): LocalDatasource = LocalDatasource(readDao, writeDao)
	
}