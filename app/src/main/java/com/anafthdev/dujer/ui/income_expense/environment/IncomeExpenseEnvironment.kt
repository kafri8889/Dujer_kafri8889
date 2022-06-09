package com.anafthdev.dujer.ui.income_expense.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class IncomeExpenseEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_MAIN) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IIncomeExpenseEnvironment {
	
	private val _financial = MutableLiveData(Financial.default)
	private val financial: LiveData<Financial> = _financial
	
	override suspend fun deleteFinancial(vararg financial: Financial) {
		appRepository.delete(*financial)
	}
	
	override suspend fun getFinancial(): Flow<Financial> {
		return financial.asFlow()
	}
	
	override suspend fun setFinancialID(id: Int) {
		_financial.value = appRepository.get(id) ?: Financial.default
	}
	
}