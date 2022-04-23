package com.anafthdev.dujer.foundation.uimode.di

import com.anafthdev.dujer.foundation.uimode.environment.IUiModeEnvironment
import com.anafthdev.dujer.foundation.uimode.environment.UiModeEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UiModeModule {
	
	@Binds
	abstract fun provideEnvironment(
		uiModeEnvironment: UiModeEnvironment
	): IUiModeEnvironment
	
}