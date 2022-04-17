package com.anafthdev.dujer.ui.app.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.ui.screen.financial.FinancialViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class DujerEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
): IDujerEnvironment {
	
	private val _isFinancialSheetShowed = MutableLiveData(false)
	private val isFinancialSheetShowed: LiveData<Boolean> = _isFinancialSheetShowed
	
	private val _financialID = MutableLiveData(Financial.default.id)
	private val financialID: LiveData<Int> = _financialID
	
	private val _financialAction = MutableLiveData(FinancialViewModel.FINANCIAL_ACTION_NEW)
	private val financialAction: LiveData<String> = _financialAction
	
	override suspend fun getIsFinancialSheetShowed(): Flow<Boolean> {
		return isFinancialSheetShowed.asFlow()
	}
	
	override suspend fun getFinancialID(): Flow<Int> {
		return financialID.asFlow()
	}
	
	override suspend fun getFinancialAction(): Flow<String> {
		return financialAction.asFlow()
	}
	
	override fun setFinancialID(id: Int) {
		_financialID.value = id
	}
	
	override fun setFinancialAction(action: String) {
		_financialAction.value = action
	}
	
	override fun showFinancialSheet() {
		_isFinancialSheetShowed.value = true
	}
	
	override fun hideFinancialSheet() {
		_isFinancialSheetShowed.value = false
	}
}