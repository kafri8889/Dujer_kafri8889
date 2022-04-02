package com.anafthdev.dujer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anafthdev.dujer.data.db.dao.FinancialDAO
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial

@Database(
	entities = [
		Financial::class,
		Category::class
	],
	version = 1,
	exportSchema = false
)
@TypeConverters(DatabaseConverter::class)
abstract class AppDatabase: RoomDatabase() {
	
	abstract fun financialDao(): FinancialDAO
	
	companion object {
		private var INSTANCE: AppDatabase? = null
		
		fun getInstance(ctx: Context): AppDatabase {
			if (INSTANCE == null) {
				synchronized(AppDatabase::class) {
					INSTANCE = Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db")
						.allowMainThreadQueries()
						.build()
				}
			}
			
			return INSTANCE!!
		}
	}
}