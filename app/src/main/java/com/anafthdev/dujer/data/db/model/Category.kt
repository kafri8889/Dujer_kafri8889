package com.anafthdev.dujer.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.dujer.R
import com.anafthdev.dujer.model.CategoryTint

@Entity(tableName = "category")
data class Category(
	@PrimaryKey val id: Int,
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "iconID") var iconID: Int,
	@ColumnInfo(name = "tint") var tint: CategoryTint,
	@ColumnInfo(name = "defaultCategory") var defaultCategory: Boolean = false
) {
	
	companion object {
		
		val default = Category(
			id = 0,
			name = "-",
			iconID = 0,
			tint = CategoryTint.tint_1,
			defaultCategory = false
		)
		
		val food = Category(
			id = 1,
			name = "Food",
			iconID = R.drawable.ic_coffee,
			tint = CategoryTint.tint_1,
			defaultCategory = true
		)
		
		val shopping = Category(
			id = 2,
			name = "Shopping",
			iconID = R.drawable.ic_shop,
			tint = CategoryTint.tint_2,
			defaultCategory = true
		)
		
		val transport = Category(
			id = 3,
			name = "Transport",
			iconID = R.drawable.ic_bus,
			tint = CategoryTint.tint_3,
			defaultCategory = true
		)
		
		val values = listOf(
			shopping,
			food,
			transport
		)
	}
}
