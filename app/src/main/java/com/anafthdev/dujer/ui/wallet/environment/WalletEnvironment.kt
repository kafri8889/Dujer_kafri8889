package com.anafthdev.dujer.ui.wallet.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class WalletEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IWalletEnvironment {
	
	private val _selectedWallet = MutableLiveData(Wallet.cash)
	private val selectedWallet: LiveData<Wallet> = _selectedWallet
	
	override suspend fun deleteWallet(wallet: Wallet) {
		appRepository.walletRepository.deleteWallet(wallet)
	}
	
	override fun getAllWallet(): Flow<List<Wallet>> {
		return appRepository.walletRepository.getAllWallet()
	}
	
	override fun getWallet(id: Int) {
		_selectedWallet.postValue(
			if (id == Wallet.cash.id) Wallet.cash else appRepository
				.walletRepository
				.get(id)
		)
	}
	
	override fun getWallet(): Flow<Wallet> {
		return selectedWallet.asFlow()
	}
	
	
}