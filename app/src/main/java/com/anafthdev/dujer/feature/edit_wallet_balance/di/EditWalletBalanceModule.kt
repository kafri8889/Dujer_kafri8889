package com.anafthdev.dujer.feature.edit_wallet_balance.di

import com.anafthdev.dujer.feature.edit_wallet_balance.environment.EditWalletBalanceEnvironment
import com.anafthdev.dujer.feature.edit_wallet_balance.environment.IEditWalletBalanceEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class EditWalletBalanceModule {
	
	@Binds
	abstract fun provideEnvironment(
		editWalletBalanceEnvironment: EditWalletBalanceEnvironment
	): IEditWalletBalanceEnvironment
	
}