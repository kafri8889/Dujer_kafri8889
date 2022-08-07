package com.anafthdev.dujer.data.model

import android.os.Parcelable
import com.anafthdev.dujer.R
import com.anafthdev.dujer.model.CategoryTint
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * to get balance => initialBalance + totalIncome - totalExpense
 */
@Parcelize
data class Wallet(
	val id: Int,
	var name: String,
	var initialBalance: Double,
	var balance: Double, // TODO: hapus?
	var iconID: Int,
	val tint: @RawValue CategoryTint,
	val defaultWallet: Boolean,
	val financials: List<Financial> = emptyList()
): Parcelable {
	companion object {
	
		val default = Wallet(
			id = 0,
			name = "",
			initialBalance = 0.0,
			balance = 0.0,
			iconID = R.drawable.ic_wallet,
			tint = CategoryTint.tint_1,
			defaultWallet = false
		)
		
		val cash = Wallet(
			id = 1,
			name = "Cash",
			initialBalance = 0.0,
			balance = 0.0,
			iconID = R.drawable.ic_wallet,
			tint = CategoryTint.tint_6,
			defaultWallet = true
		)
	}
	
}
