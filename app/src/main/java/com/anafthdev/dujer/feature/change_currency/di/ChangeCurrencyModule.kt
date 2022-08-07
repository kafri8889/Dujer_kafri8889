package com.anafthdev.dujer.feature.change_currency.di

import com.anafthdev.dujer.feature.change_currency.environment.ChangeCurrencyEnvironment
import com.anafthdev.dujer.feature.change_currency.environment.IChangeCurrencyEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ChangeCurrencyModule {
	
	@Binds
	abstract fun provideEnvironment(
		changeCurrencyEnvironment: ChangeCurrencyEnvironment
	): IChangeCurrencyEnvironment
	
}