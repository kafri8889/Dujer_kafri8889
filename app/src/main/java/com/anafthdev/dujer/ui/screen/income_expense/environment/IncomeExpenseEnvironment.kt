package com.anafthdev.dujer.ui.screen.income_expense.environment

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class IncomeExpenseEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IIncomeExpenseEnvironment {
	
	override suspend fun deleteFinancial(financial: Financial) {
		appRepository.delete(financial)
	}
	
	override suspend fun getIncomeFinancialList(): Flow<List<Financial>> {
		return appRepository.incomeRepository.getIncome()
	}
	
	override suspend fun getExpenseFinancialList(): Flow<List<Financial>> {
		return appRepository.expenseRepository.getExpense()
	}
	
	
}