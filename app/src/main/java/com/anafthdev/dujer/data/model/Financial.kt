package com.anafthdev.dujer.data.model

import android.os.Parcelable
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.model.Currency
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Financial(
	val id: Int,
	var name: String,
	var amount: Double,
	var type: @RawValue FinancialType,
	var walletID: Int,
	var category: Category,
	var currency: Currency,
	var dateCreated: Long,
): Parcelable {
	override fun hashCode(): Int {
		return super.hashCode()
			.plus(id)
			.plus(amount.toInt())
			.plus(walletID)
			.plus(category.id)
			.plus(dateCreated.toInt())
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as Financial
		
		if (id != other.id) return false
		if (name != other.name) return false
		if (amount != other.amount) return false
		if (type != other.type) return false
		if (walletID != other.walletID) return false
		if (category != other.category) return false
		if (currency != other.currency) return false
		if (dateCreated != other.dateCreated) return false
		
		return true
	}
	
	companion object {
		val default = Financial(
			id = 0,
			name = "",
			amount = 0.0,
			type = FinancialType.INCOME,
			walletID = Wallet.cash.id,
			category = Category.default,
			currency = Currency.DOLLAR,
			dateCreated = System.currentTimeMillis()
		)
	}
}
