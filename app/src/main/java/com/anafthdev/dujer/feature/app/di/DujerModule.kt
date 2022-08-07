package com.anafthdev.dujer.feature.app.di

import com.anafthdev.dujer.feature.app.environment.DujerEnvironment
import com.anafthdev.dujer.feature.app.environment.IDujerEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DujerModule {
	
	@Binds
	abstract fun provideEnvironment(
		environment: DujerEnvironment
	): IDujerEnvironment
	
}