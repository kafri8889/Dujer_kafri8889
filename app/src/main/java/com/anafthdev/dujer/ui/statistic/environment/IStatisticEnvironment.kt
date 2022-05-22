package com.anafthdev.dujer.ui.statistic.environment

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IStatisticEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getWallet(): Flow<Wallet>
	
	fun getPieEntry(): Flow<List<PieEntry>>
	
	fun getIncomeTransaction(): Flow<List<Financial>>
	
	fun getExpenseTransaction(): Flow<List<Financial>>
	
	fun getAvailableCategory(): Flow<List<Category>>
	
	fun getSelectedFinancialType(): Flow<FinancialType>
	
	
	
	suspend fun getWallet(walletID: Int)
	
	suspend fun setSelectedDate(date: Long)
	
	suspend fun setSelectedFinancialType(type: FinancialType)
	
}