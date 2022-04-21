package com.anafthdev.dujer.foundation.localized.di

import com.anafthdev.dujer.foundation.localized.environment.ILocalizedEnvironment
import com.anafthdev.dujer.foundation.localized.environment.LocalizedEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocalizedModule {
	
	@Binds
	abstract fun provideEnvironment(
		localizedEnvironment: LocalizedEnvironment
	): ILocalizedEnvironment
	
}