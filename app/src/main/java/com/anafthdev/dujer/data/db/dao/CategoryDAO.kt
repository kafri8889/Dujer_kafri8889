package com.anafthdev.dujer.data.db.dao

import androidx.room.*
import com.anafthdev.dujer.data.db.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {
	
	@Query("SELECT * FROM category_table")
	fun getAllCategory(): Flow<List<Category>>
	
	@Query("SELECT * FROM category_table WHERE id LIKE :mID")
	suspend fun get(mID: Int): Category?
	
	@Update
	suspend fun update(vararg category: Category)
	
	@Delete
	suspend fun delete(vararg category: Category)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg category: Category)
	
}