package com.anafthdev.dujer.ui.dashboard.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.util.AppUtil
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

class DashboardEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IDashboardEnvironment {
	
	private val _financial = MutableLiveData(Financial.default)
	private val financial: LiveData<Financial> = _financial
	
	private val _financialAction = MutableLiveData(FinancialAction.NEW)
	private val financialAction: LiveData<String> = _financialAction
	
	override suspend fun getFinancial(): Flow<Financial> {
		return financial.asFlow()
	}
	
	override suspend fun getFinancialAction(): Flow<String> {
		return financialAction.asFlow()
	}
	
	override suspend fun setFinancialID(id: Int) {
		_financial.postValue(appRepository.get(id) ?: Financial.default)
	}
	
	override suspend fun insertWallet(wallet: Wallet) {
		appRepository.walletRepository.insertWallet(wallet)
	}
	
	override fun setFinancialAction(action: String) {
		_financialAction.postValue(action)
	}
	
}