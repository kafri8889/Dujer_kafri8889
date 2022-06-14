package com.anafthdev.dujer.data.db.dao

import androidx.room.*
import com.anafthdev.dujer.data.db.model.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDAO {
	
	@Query("SELECT * FROM budget_table")
	fun getAll(): Flow<List<Budget>>
	
	@Query("SELECT EXISTS(SELECT * FROM budget_table WHERE id = :mID)")
	fun isExists(mID: Int): Boolean
	
	@Query("SELECT * FROM budget_table WHERE id LIKE :mID")
	suspend fun get(mID: Int): Budget?
	
	@Update
	suspend fun update(vararg budget: Budget)
	
	@Delete
	suspend fun delete(vararg budget: Budget)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg budget: Budget)
	
}