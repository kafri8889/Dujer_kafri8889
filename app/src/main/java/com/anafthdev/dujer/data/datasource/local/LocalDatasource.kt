package com.anafthdev.dujer.data.datasource.local

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.datasource.local.db.DujerReadDao
import com.anafthdev.dujer.data.datasource.local.db.DujerWriteDao
import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.foundation.extension.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDatasource @Inject constructor(
	private val readDao: DujerReadDao,
	private val writeDao: DujerWriteDao
) {
	
	// Financial
	
	fun getAllFinancial(): Flow<List<Financial>> {
		return readDao.getAllFinancial()
			.filterNotNull()
			.distinctUntilChanged()
			.map { it.toFinancial() }
	}

	fun getFinancialByID(mID: Int): Flow<Financial> {
		return readDao.getFinancialByID(mID)
			.filterNotNull()
			.map { it.toFinancial() }
	}
	
	fun isFinancialExists(financialID: Int): Boolean {
		return readDao.isFinancialExists(financialID)
	}
	
	fun getIncomeTransaction(): Flow<List<Financial>> {
		return getFinancialByType(FinancialType.INCOME.ordinal)
	}
	
	fun getExpenseTransaction(): Flow<List<Financial>> {
		return getFinancialByType(FinancialType.EXPENSE.ordinal)
	}
	
	private fun getFinancialByType(mType: Int): Flow<List<Financial>> {
		return readDao.getFinancialByType(mType)
			.filterNotNull()
			.distinctUntilChanged()
			.map { it.toFinancial() }
	}
	
	suspend fun deleteFinancial(vararg financial: Financial) {
		writeDao.deleteFinancialDb(*financial.toFinancialDb())
	}
	
	suspend fun updateFinancial(vararg financial: Financial) {
		writeDao.updateFinancialDb(*financial.toFinancialDb())
	}
	
	suspend fun insertFinancial(vararg financial: Financial) {
		writeDao.insertFinancialDb(*financial.toFinancialDb())
	}
	
	
	
	
	// Category
	
	fun getAllCategory(): Flow<List<Category>> {
		return readDao.getAllCategory()
			.filterNotNull()
			.distinctUntilChanged()
			.map { it.toCategory() }
	}
	
	fun getCategoryByID(mID: Int): Category? {
		return readDao.getCategoryByID(mID)?.toCategory()
	}
	
	suspend fun deleteCategory(vararg category: Category) {
		writeDao.deleteCategoryDb(*category.toCategoryDb())
	}
	
	suspend fun updateCategory(vararg category: Category) {
		writeDao.updateCategoryDb(*category.toCategoryDb())
	}
	
	suspend fun insertCategory(vararg category: Category) {
		writeDao.insertCategoryDb(*category.toCategoryDb())
	}
	
	
	
	
	// Wallet
	
	fun getAllWallet(): Flow<List<Wallet>> {
		return readDao.getAllWallet()
			.filterNotNull()
			.distinctUntilChanged()
			.map { it.toWallet() }
	}
	
	fun getWalletByID(mID: Int): Wallet? {
		return readDao.getWalletByID(mID)?.toWallet()
	}
	
	suspend fun deleteWallet(vararg wallet: Wallet) {
		writeDao.deleteWalletDb(*wallet.toWalletDb())
	}
	
	suspend fun updateWallet(vararg wallet: Wallet) {
		writeDao.updateWalletDb(*wallet.toWalletDb())
	}
	
	suspend fun insertWallet(vararg wallet: Wallet) {
		writeDao.insertWalletDb(*wallet.toWalletDb())
	}
	
	
	
	
	// Budget
	
	fun getAllBudget(): Flow<List<Budget>> {
		return readDao.getAllBudget()
			.filterNotNull()
			.distinctUntilChanged()
			.map { it.toBudget() }
	}
	
	fun getBudgetByID(mID: Int): Budget? {
		return readDao.getBudgetByID(mID)?.toBudget()
	}
	
	fun isBudgetExists(budgetID: Int): Boolean {
		return readDao.isBudgetExists(budgetID)
	}
	
	suspend fun deleteBudget(vararg budget: Budget) {
		writeDao.deleteBudgetDb(*budget.toBudgetDb())
	}
	
	suspend fun updateBudget(vararg budget: Budget) {
		writeDao.updateBudgetDb(*budget.toBudgetDb())
	}
	
	suspend fun insertBudget(vararg budget: Budget) {
		writeDao.insertBudgetDb(*budget.toBudgetDb())
	}
	
}