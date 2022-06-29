package com.anafthdev.dujer.data.db.model

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.saveable.listSaver
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.CategoryIcons
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.model.CategoryTint

@Entity(tableName = "category_table")
data class Category(
	@PrimaryKey val id: Int,
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "iconID") var iconID: Int,
	@ColumnInfo(name = "tint") var tint: CategoryTint,
	@ColumnInfo(name = "type") var type: FinancialType,
	@ColumnInfo(name = "defaultCategory") var defaultCategory: Boolean = false
): Parcelable {
	
	constructor(parcel: Parcel) : this(
		parcel.readInt(),
		parcel.readString()!!,
		parcel.readInt(),
		parcel.readParcelable(CategoryTint::class.java.classLoader)!!,
		FinancialType.values()[parcel.readInt()],
		parcel.readByte() != 0.toByte()
	)
	
	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(id)
		parcel.writeString(name)
		parcel.writeInt(iconID)
		parcel.writeParcelable(tint, flags)
		parcel.writeInt(type.ordinal)
		parcel.writeByte(if (defaultCategory) 1 else 0)
	}
	
	override fun describeContents(): Int {
		return 0
	}
	
	companion object CREATOR : Parcelable.Creator<Category> {
		override fun createFromParcel(parcel: Parcel): Category {
			return Category(parcel)
		}
		
		override fun newArray(size: Int): Array<Category?> {
			return arrayOfNulls(size)
		}
		
		val Saver = listSaver<Category, Any>(
			save = {
				listOf(
					it.id,
					it.name,
					it.iconID,
					it.tint.toJSON(),
					it.type,
					it.defaultCategory,
				)
			},
			restore = {
				Category(
					id = it[0] as Int,
					name = it[1] as String,
					iconID = it[2] as Int,
					tint = CategoryTint.fromJSON(it[3] as String),
					type = it[4] as FinancialType,
					defaultCategory = it[5] as Boolean
				)
			}
		)
		
		val default = Category(
			id = 0,
			name = "-",
			iconID = CategoryIcons.other,
			tint = CategoryTint.tint_1,
			type = FinancialType.INCOME,
			defaultCategory = false
		)
		
		val transaction = Category(
			id = -9,
			name = "Transaction",
			iconID = CategoryIcons.cardCoin,
			tint = CategoryTint.transaction,
			type = FinancialType.EXPENSE,
			defaultCategory = true
		)
		
		val food = Category(
			id = 1,
			name = "Food",
			iconID = CategoryIcons.coffee,
			tint = CategoryTint.tint_1,
			type = FinancialType.EXPENSE,
			defaultCategory = true
		)
		
		val shopping = Category(
			id = 2,
			name = "Shopping",
			iconID = CategoryIcons.shoppingCart,
			tint = CategoryTint.tint_2,
			type = FinancialType.EXPENSE,
			defaultCategory = true
		)
		
		val transport = Category(
			id = 3,
			name = "Transport",
			iconID = CategoryIcons.bus,
			tint = CategoryTint.tint_3,
			type = FinancialType.EXPENSE,
			defaultCategory = true
		)
		
		val electronic = Category(
			id = 4,
			name = "Electronic",
			iconID = CategoryIcons.electronic,
			tint = CategoryTint.tint_4,
			type = FinancialType.EXPENSE,
			defaultCategory = true
		)
		
		val bill = Category(
			id = 5,
			name = "Bill",
			iconID = CategoryIcons.bill,
			tint = CategoryTint.tint_5,
			type = FinancialType.EXPENSE,
			defaultCategory = true
		)
		
		val salary = Category(
			id = 6,
			name = "Salary",
			iconID = CategoryIcons.salary,
			tint = CategoryTint.tint_6,
			type = FinancialType.INCOME,
			defaultCategory = true
		)
		
		val investment = Category(
			id = 7,
			name = "Investment",
			iconID = CategoryIcons.investment,
			tint = CategoryTint.tint_7,
			type = FinancialType.INCOME,
			defaultCategory = true
		)
		
		val entertainment = Category(
			id = 8,
			name = "Entertainment",
			iconID = CategoryIcons.entertainment,
			tint = CategoryTint.tint_8,
			type = FinancialType.EXPENSE,
			defaultCategory = true
		)
		
		val gadget = Category(
			id = 9,
			name = "Gadget",
			iconID = CategoryIcons.monitorMobile,
			tint = CategoryTint.tint_9,
			type = FinancialType.EXPENSE,
			defaultCategory = true
		)
		
		val lottery = Category(
			id = 10,
			name = "Lottery",
			iconID = R.drawable.ic_ticket_star,
			tint = CategoryTint.tint_10,
			type = FinancialType.INCOME,
			defaultCategory = true
		)
		
		val bonus = Category(
			id = 11,
			name = "Bonus",
			iconID = CategoryIcons.coin,
			tint = CategoryTint.tint_11,
			type = FinancialType.INCOME,
			defaultCategory = true
		)
		
		val award = Category(
			id = 12,
			name = "Award",
			iconID = R.drawable.ic_medal,
			tint = CategoryTint.tint_12,
			type = FinancialType.INCOME,
			defaultCategory = true
		)
		
		val otherExpense = Category(
			id = 20,
			name = "Other",
			iconID = CategoryIcons.other,
			tint = CategoryTint.tint_20,
			type = FinancialType.EXPENSE,
			defaultCategory = true
		)
		
		val otherIncome = Category(
			id = 20_1,
			name = "Other",
			iconID = CategoryIcons.other,
			tint = CategoryTint.tint_20,
			type = FinancialType.INCOME,
			defaultCategory = true
		)
		
		val values = listOf(
			shopping,
			food,
			transport,
			electronic,
			bill,
			salary,
			investment,
			entertainment,
			gadget,
			lottery,
			bonus,
			award,
			otherExpense,
			otherIncome
		)
		
		val incomeValues = listOf(
			salary,
			investment,
			lottery,
			bonus,
			award,
			otherIncome
		)
		
		val expenseValues = listOf(
			shopping,
			food,
			transport,
			electronic,
			bill,
			entertainment,
			gadget,
			otherExpense
		)
	}
	
}
