package com.anafthdev.dujer.ui.wallet.di

import com.anafthdev.dujer.ui.wallet.environment.IWalletEnvironment
import com.anafthdev.dujer.ui.wallet.environment.WalletEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class WalletModule {
	
	@Binds
	abstract fun provideEnvironment(
		walletEnvironment: WalletEnvironment
	): IWalletEnvironment
	
}