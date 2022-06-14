package com.anafthdev.dujer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anafthdev.dujer.data.db.dao.BudgetDAO
import com.anafthdev.dujer.data.db.dao.CategoryDAO
import com.anafthdev.dujer.data.db.dao.FinancialDAO
import com.anafthdev.dujer.data.db.dao.WalletDAO
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet

@Database(
	entities = [
		Financial::class,
		Category::class,
		Wallet::class,
		Budget::class
	],
	version = 1,
	exportSchema = false
)
@TypeConverters(DatabaseConverter::class)
abstract class AppDatabase: RoomDatabase() {
	
	abstract fun financialDAO(): FinancialDAO
	abstract fun categoryDAO(): CategoryDAO
	abstract fun walletDAO(): WalletDAO
	abstract fun budgetDAO(): BudgetDAO
	
	companion object {
		private var INSTANCE: AppDatabase? = null
		
		fun getInstance(ctx: Context): AppDatabase {
			if (INSTANCE == null) {
				synchronized(AppDatabase::class) {
					INSTANCE = Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db")
						.build()
				}
			}
			
			return INSTANCE!!
		}
	}
}