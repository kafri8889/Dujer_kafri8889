package com.anafthdev.dujer.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.CategoryIcons
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
			iconID = CategoryIcons.coffee,
			tint = CategoryTint.tint_1,
			defaultCategory = true
		)
		
		val shopping = Category(
			id = 2,
			name = "Shopping",
			iconID = CategoryIcons.shoppingCart,
			tint = CategoryTint.tint_2,
			defaultCategory = true
		)
		
		val transport = Category(
			id = 3,
			name = "Transport",
			iconID = CategoryIcons.bus,
			tint = CategoryTint.tint_3,
			defaultCategory = true
		)
		
		val electronic = Category(
			id = 4,
			name = "Electronic",
			iconID = CategoryIcons.electronic,
			tint = CategoryTint.tint_4,
			defaultCategory = true
		)
		
		val bill = Category(
			id = 5,
			name = "Bill",
			iconID = CategoryIcons.bill,
			tint = CategoryTint.tint_5,
			defaultCategory = true
		)
		
		val salary = Category(
			id = 6,
			name = "Salary",
			iconID = CategoryIcons.salary,
			tint = CategoryTint.tint_6,
			defaultCategory = true
		)
		
		val investment = Category(
			id = 7,
			name = "Investment",
			iconID = CategoryIcons.investment,
			tint = CategoryTint.tint_7,
			defaultCategory = true
		)
		
		val entertainment = Category(
			id = 8,
			name = "Entertainment",
			iconID = CategoryIcons.entertainment,
			tint = CategoryTint.tint_8,
			defaultCategory = true
		)
		
		val gadget = Category(
			id = 9,
			name = "Gadget",
			iconID = CategoryIcons.monitorMobile,
			tint = CategoryTint.tint_9,
			defaultCategory = true
		)
		
		val other = Category(
			id = 20,
			name = "Other",
			iconID = CategoryIcons.other,
			tint = CategoryTint.tint_20,
			defaultCategory = true
		)
		
		val values = listOf(
			shopping,
			food,
			transport,
			electronic,
			bill,
			investment,
			entertainment,
			gadget,
			other
		)
	}
}
