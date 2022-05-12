package com.anafthdev.dujer.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.dujer.R
import com.anafthdev.dujer.model.CategoryTint

@Entity(tableName = "wallet_table")
data class Wallet(
	@PrimaryKey val id: Int,
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "balance") var balance: Double,
	@ColumnInfo(name = "iconID") var iconID: Int,
	@ColumnInfo(name = "tint") val categoryTint: CategoryTint,
	@ColumnInfo(name = "defaultWallet") val defaultWallet: Boolean,
) {
	companion object {
		val default = Wallet(
			id = 0,
			name = "",
			balance = 0.0,
			iconID = R.drawable.ic_wallet,
			categoryTint = CategoryTint.tint_1,
			defaultWallet = false
		)
		
		val cash = Wallet(
			id = 1,
			name = "Cash",
			balance = 0.0,
			iconID = R.drawable.ic_wallet,
			categoryTint = CategoryTint.tint_6,
			defaultWallet = true
		)
	}
}
