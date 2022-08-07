package com.anafthdev.dujer.data.repository

import com.anafthdev.dujer.data.datasource.local.LocalDatasource
import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
	private val localDatasource: LocalDatasource
) {
	
	// Financial
	
	fun getAllFinancial(): Flow<List<Financial>> {
		return localDatasource.getAllFinancial()
	}
	
	fun getFinancialByID(mID: Int): Flow<Financial> {
		return localDatasource.getFinancialByID(mID)
	}
	
	fun isFinancialExists(financialID: Int): Boolean {
		return localDatasource.isFinancialExists(financialID)
	}
	
	fun getIncomeTransaction(): Flow<List<Financial>> {
		return localDatasource.getIncomeTransaction()
	}
	
	fun getExpenseTransaction(): Flow<List<Financial>> {
		return localDatasource.getExpenseTransaction()
	}
	
	suspend fun deleteFinancial(vararg financial: Financial) {
		localDatasource.deleteFinancial(*financial)
	}
	
	suspend fun updateFinancial(vararg financial: Financial) {
		localDatasource.updateFinancial(*financial)
	}
	
	suspend fun insertFinancial(vararg financial: Financial) {
		localDatasource.insertFinancial(*financial)
	}
	
	
	
	
	// Category
	
	fun getAllCategory(): Flow<List<Category>> {
		return localDatasource.getAllCategory()
	}
	
	fun getCategoryByID(mID: Int): Category? {
		return localDatasource.getCategoryByID(mID)
	}
	
	suspend fun deleteCategory(vararg category: Category) {
		localDatasource.deleteCategory(*category)
	}
	
	suspend fun updateCategory(vararg category: Category) {
		localDatasource.updateCategory(*category)
	}
	
	suspend fun insertCategory(vararg category: Category) {
		localDatasource.insertCategory(*category)
	}
	
	
	
	
	// Wallet
	
	fun getAllWallet(): Flow<List<Wallet>> {
		return localDatasource.getAllWallet()
	}
	
	fun getWalletByID(mID: Int): Wallet? {
		return localDatasource.getWalletByID(mID)
	}
	
	suspend fun deleteWallet(vararg wallet: Wallet) {
		localDatasource.deleteWallet(*wallet)
	}
	
	suspend fun updateWallet(vararg wallet: Wallet) {
		localDatasource.updateWallet(*wallet)
	}
	
	suspend fun insertWallet(vararg wallet: Wallet) {
		localDatasource.insertWallet(*wallet)
	}
	
	
	
	
	// Budget
	
	fun getAllBudget(): Flow<List<Budget>> {
		return localDatasource.getAllBudget()
	}
	
	fun getBudgetByID(mID: Int): Budget? {
		return localDatasource.getBudgetByID(mID)
	}
	
	fun isBudgetExists(budgetID: Int): Boolean {
		return localDatasource.isBudgetExists(budgetID)
	}
	
	suspend fun deleteBudget(vararg budget: Budget) {
		localDatasource.deleteBudget(*budget)
	}
	
	suspend fun updateBudget(vararg budget: Budget) {
		localDatasource.updateBudget(*budget)
	}
	
	suspend fun insertBudget(vararg budget: Budget) {
		localDatasource.insertBudget(*budget)
	}
}