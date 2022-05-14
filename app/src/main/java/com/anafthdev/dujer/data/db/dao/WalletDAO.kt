package com.anafthdev.dujer.data.db.dao

import androidx.room.*
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDAO {
	
	@Query("SELECT * FROM wallet_table")
	fun getAllWallet(): Flow<List<Wallet>>
	
	@Query("SELECT * FROM wallet_table WHERE id LIKE :mID")
	fun get(mID: Int): Wallet
	
	@Query(
		"SELECT * FROM financial_table WHERE " +
				"(financial_table.type = :financialType) " +
				"AND (financial_table.walletID = :walletID)"
	)
	fun getFinancialTransaction(walletID: Int, financialType: Int): Flow<List<Financial>>
	
	@Update
	suspend fun update(vararg wallet: Wallet)
	
	@Delete
	suspend fun delete(vararg wallet: Wallet)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(vararg wallet: Wallet)
	
}