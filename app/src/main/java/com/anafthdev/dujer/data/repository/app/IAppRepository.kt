package com.anafthdev.dujer.data.repository.app

import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.category.ICategoryRepository
import com.anafthdev.dujer.data.repository.expense.IExpenseRepository
import com.anafthdev.dujer.data.repository.income.IIncomeRepository

interface IAppRepository {
	val appDatastore: AppDatastore
	val categoryRepository: ICategoryRepository
	val incomeRepository: IIncomeRepository
	val expenseRepository: IExpenseRepository
	
	suspend fun get(id: Int): Financial?
	
	suspend fun update(vararg financials: Financial)
	
	suspend fun delete(vararg financials: Financial)
	
	suspend fun insert(vararg financials: Financial)
}

