package com.anafthdev.dujer.data.db.dao

import androidx.room.*
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialDAO {
	
	@Query("SELECT * FROM financial_table")
	fun getAll(): Flow<List<Financial>>
	
	@Query("SELECT * FROM financial_table WHERE type= :mType")
	fun getByType(mType: Int): Flow<List<Financial>>
	
	@Query("SELECT * FROM financial_table WHERE id= :mID")
	suspend fun get(mID: Int): Financial?
	
	@Update
	suspend fun update(vararg financial: Financial)
	
	@Delete
	suspend fun delete(vararg financial: Financial)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg financial: Financial)
	
	suspend fun getIncome(): Flow<List<Financial>> = getByType(FinancialType.INCOME.ordinal)
	suspend fun getExpense(): Flow<List<Financial>> = getByType(FinancialType.EXPENSE.ordinal)
}