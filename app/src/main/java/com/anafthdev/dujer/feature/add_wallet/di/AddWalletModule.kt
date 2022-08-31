package com.anafthdev.dujer.feature.add_wallet.di

import com.anafthdev.dujer.feature.add_wallet.environment.AddWalletEnvironment
import com.anafthdev.dujer.feature.add_wallet.environment.IAddWalletEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AddWalletModule {
	
	@Binds
	abstract fun provideEnvironment(
		addWalletEnvironment: AddWalletEnvironment
	): IAddWalletEnvironment
	
}