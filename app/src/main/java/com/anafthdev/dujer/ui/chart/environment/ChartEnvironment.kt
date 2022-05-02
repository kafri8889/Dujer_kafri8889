package com.anafthdev.dujer.ui.chart.environment

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.util.AppUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

class ChartEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IChartEnvironment {
	
	private val monthYearFormatter = SimpleDateFormat("MMyyyy", AppUtil.deviceLocale)
	
	override suspend fun getIncomeFinancialList(): Flow<List<Financial>> {
		return appRepository.expenseRepository.getExpense()
	}
	
	override suspend fun getExpenseFinancialList(): Flow<List<Financial>> {
		return appRepository.incomeRepository.getIncome()
	}
	
}