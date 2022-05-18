package com.anafthdev.dujer.ui.statistic.environment

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IStatisticEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getWallet(walletID: Int)
	
	fun getWallet(): Flow<Wallet>
	
	fun getIncomeTransaction(): Flow<List<Financial>>
	
	fun getExpenseTransaction(): Flow<List<Financial>>
	
}