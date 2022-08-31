package com.anafthdev.dujer.data.datasource.local.db

import androidx.room.*
import com.anafthdev.dujer.data.model.BudgetDb
import com.anafthdev.dujer.data.model.CategoryDb
import com.anafthdev.dujer.data.model.FinancialDb
import com.anafthdev.dujer.data.model.WalletDb

@Dao
abstract class DujerWriteDao {
	
	// Financial
	
	@Delete
	abstract suspend fun deleteFinancialDb(vararg financialDb: FinancialDb)
	
	@Update(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun updateFinancialDb(vararg financialDb: FinancialDb)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun insertFinancialDb(vararg financialDb: FinancialDb)
	
	
	
	
	// Category
	
	@Delete
	abstract suspend fun deleteCategoryDb(vararg categoryDb: CategoryDb)
	
	@Update(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun updateCategoryDb(vararg categoryDb: CategoryDb)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun insertCategoryDb(vararg categoryDb: CategoryDb)
	
	
	
	
	// Category
	
	@Delete
	abstract suspend fun deleteWalletDb(vararg walletDb: WalletDb)
	
	@Update(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun updateWalletDb(vararg walletDb: WalletDb)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun insertWalletDb(vararg walletDb: WalletDb)
	
	
	
	
	// Budget
	
	@Delete
	abstract suspend fun deleteBudgetDb(vararg budgetDb: BudgetDb)
	
	@Update(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun updateBudgetDb(vararg budgetDb: BudgetDb)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun insertBudgetDb(vararg budgetDb: BudgetDb)
	
}