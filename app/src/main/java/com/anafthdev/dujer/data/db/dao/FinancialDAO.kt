package com.anafthdev.dujer.data.db.dao

import androidx.room.*
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialDAO {
	
	@Query("SELECT * FROM financial")
	fun getAll(): Flow<List<Financial>>
	
	@Query("SELECT * FROM financial WHERE type= :mType")
	fun getByType(mType: Int): Flow<List<Financial>>
	
	@Query("SELECT * FROM financial WHERE id= :mID")
	fun get(mID: Int): Financial
	
	@Update
	fun update(vararg financial: Financial)
	
	@Delete
	fun delete(vararg financial: Financial)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(vararg financial: Financial)
	
	fun getIncome(): Flow<List<Financial>> = getByType(FinancialType.INCOME.ordinal)
	fun getExpense(): Flow<List<Financial>> = getByType(FinancialType.EXPENSE.ordinal)
}