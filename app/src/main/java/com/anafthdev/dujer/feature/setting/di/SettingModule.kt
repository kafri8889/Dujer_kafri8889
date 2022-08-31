package com.anafthdev.dujer.feature.setting.di

import com.anafthdev.dujer.feature.setting.environment.ISettingEnvironment
import com.anafthdev.dujer.feature.setting.environment.SettingEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SettingModule {
	
	@Binds
	abstract fun provideEnvironment(
		settingEnvironment: SettingEnvironment
	): ISettingEnvironment
}