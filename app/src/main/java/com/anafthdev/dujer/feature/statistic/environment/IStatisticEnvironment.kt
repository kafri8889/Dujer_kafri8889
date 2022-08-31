package com.anafthdev.dujer.feature.statistic.environment

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IStatisticEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getPieEntry(): Flow<List<PieEntry>>
	
	fun getIncomeTransaction(): Flow<List<Financial>>
	
	fun getExpenseTransaction(): Flow<List<Financial>>
	
	fun getAvailableCategory(): Flow<List<Category>>
	
	fun getSelectedFinancialType(): Flow<FinancialType>
	
	fun getWallet(walletID: Int): Flow<Wallet>
	
	
	
	suspend fun setWallet(walletID: Int)
	
	suspend fun setSelectedDate(date: Long)
	
	suspend fun setSelectedFinancialType(type: FinancialType)
	
}