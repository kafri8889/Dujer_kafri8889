package com.anafthdev.dujer.data

import android.content.Context
import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.expense.ExpenseRepository
import com.anafthdev.dujer.data.expense.IExpenseRepository
import com.anafthdev.dujer.data.income.IIncomeRepository
import com.anafthdev.dujer.data.income.IncomeRepository
import javax.inject.Inject

interface AppRepository {
	val appDatastore: AppDatastore
	val incomeRepository: IIncomeRepository
	val expenseRepository: IExpenseRepository
}

class AppRepositoryImpl @Inject constructor(
	private val appDatabase: AppDatabase,
	override val appDatastore: AppDatastore
): AppRepository {
	
	override val incomeRepository: IIncomeRepository by lazy {
		IncomeRepository(appDatabase)
	}
	
	override val expenseRepository: IExpenseRepository by lazy {
		ExpenseRepository(appDatabase)
	}
}
