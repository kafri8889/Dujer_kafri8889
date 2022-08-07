package com.anafthdev.dujer.data.datasource.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anafthdev.dujer.data.model.BudgetDb
import com.anafthdev.dujer.data.model.CategoryDb
import com.anafthdev.dujer.data.model.FinancialDb
import com.anafthdev.dujer.data.model.WalletDb

@Database(
	entities = [
		FinancialDb::class,
		CategoryDb::class,
		WalletDb::class,
		BudgetDb::class
	],
	version = 1,
	exportSchema = false
)
@TypeConverters(DatabaseConverter::class)
abstract class AppDatabase: RoomDatabase() {
	
	abstract fun readDao(): DujerReadDao
	abstract fun writeDao(): DujerWriteDao
	
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