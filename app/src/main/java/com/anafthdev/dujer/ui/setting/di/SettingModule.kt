package com.anafthdev.dujer.ui.setting.di

import android.content.Context
import com.anafthdev.dujer.data.datastore.di.DatastoreModule
import com.anafthdev.dujer.ui.setting.SettingViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SettingModule {
	
	@Provides
	fun provideSettingViewModel(
		@ApplicationContext context: Context
	): SettingViewModel = SettingViewModel(
		DatastoreModule.provideAppDatastore(context)
	)
}