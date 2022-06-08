package com.anafthdev.dujer.data.db.dao

import androidx.room.*
import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDAO {
	
	@Query("SELECT * FROM wallet_table")
	fun getAllWallet(): Flow<List<Wallet>>
	
	@Query("SELECT * FROM wallet_table WHERE id LIKE :mID")
	fun get(mID: Int): Wallet?
	
	@Update(onConflict = OnConflictStrategy.REPLACE)
	suspend fun update(vararg wallet: Wallet)
	
	@Delete
	suspend fun delete(vararg wallet: Wallet)
	
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insert(vararg wallet: Wallet)
	
}