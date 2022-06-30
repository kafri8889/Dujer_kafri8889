package com.anafthdev.dujer.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_table")
data class Budget(
	@PrimaryKey(autoGenerate = false) val id: Int,
	@ColumnInfo(name = "max") val max: Double,
	@ColumnInfo(name = "category") val category: Category,
	@ColumnInfo(name = "remaining") val remaining: Double = 0.0,
	@ColumnInfo(name = "isMaxReached") val isMaxReached: Boolean = false,
) {
	
	companion object {
		val defalut = Budget(
			id = -1,
			max = 0.0,
			category = Category.default,
			remaining = 0.0,
			isMaxReached = false
		)
	}
	
}
