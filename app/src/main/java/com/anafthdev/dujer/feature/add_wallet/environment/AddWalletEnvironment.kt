package com.anafthdev.dujer.feature.add_wallet.environment

import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.data.repository.Repository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class AddWalletEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val repository: Repository
): IAddWalletEnvironment {
	
	override suspend fun save(wallet: Wallet) {
		repository.insertWallet(wallet)
	}
	
}