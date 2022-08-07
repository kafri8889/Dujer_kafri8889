package com.anafthdev.dujer.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.relation.BudgetWithCategory
import com.anafthdev.dujer.data.model.relation.CategoryWithFinancial
import com.anafthdev.dujer.data.model.relation.FinancialWithCategory
import com.anafthdev.dujer.data.model.relation.WalletWithFinancial
import kotlinx.coroutines.flow.Flow

@Dao
interface DujerReadDao {
	
	// Financial
	
	@Transaction
	@Query("SELECT * FROM financial_table")
	fun getAllFinancial(): Flow<List<FinancialWithCategory>>
	
	@Transaction
	@Query("SELECT * FROM financial_table WHERE financial_type= :mType")
	fun getFinancialByType(mType: Int): Flow<List<FinancialWithCategory>>
	
	@Query("SELECT * FROM financial_table WHERE financial_id= :mID")
	fun getFinancialByID(mID: Int): Flow<FinancialWithCategory>
	
	@Query("SELECT EXISTS(SELECT * FROM financial_table WHERE financial_id = :financialID)")
	fun isFinancialExists(financialID: Int): Boolean
	
	fun getIncomeTransaction(): Flow<List<FinancialWithCategory>> = getFinancialByType(FinancialType.INCOME.ordinal)
	fun getExpenseTransaction(): Flow<List<FinancialWithCategory>> = getFinancialByType(FinancialType.EXPENSE.ordinal)
	
	
	
	
	// Category
	
	@Transaction
	@Query("SELECT * FROM category_table")
	fun getAllCategory(): Flow<List<CategoryWithFinancial>>
	
	@Transaction
	@Query("SELECT * FROM category_table WHERE category_id LIKE :mID")
	fun getCategoryByID(mID: Int): CategoryWithFinancial?
	
	
	
	
	// Wallet
	
	@Transaction
	@Query("SELECT * FROM wallet_table")
	fun getAllWallet(): Flow<List<WalletWithFinancial>>
	
	@Transaction
	@Query("SELECT * FROM wallet_table WHERE wallet_id LIKE :mID")
	fun getWalletByID(mID: Int): WalletWithFinancial?
	
	
	
	
	// Budget
	
	@Transaction
	@Query("SELECT * FROM budget_table")
	fun getAllBudget(): Flow<List<BudgetWithCategory>>
	
	@Query("SELECT * FROM budget_table WHERE budget_id= :mID")
	fun getBudgetByID(mID: Int): BudgetWithCategory?
	
	@Query("SELECT EXISTS(SELECT * FROM budget_table WHERE budget_id = :budgetID)")
	fun isBudgetExists(budgetID: Int): Boolean
	
}