package com.anafthdev.dujer.feature.edit_wallet_balance.environment

import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.data.repository.Repository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class EditWalletBalanceEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val repository: Repository
): IEditWalletBalanceEnvironment {
	
	override fun getWallet(id: Int): Flow<Wallet> {
		return repository.getWalletByID(id)
	}
}