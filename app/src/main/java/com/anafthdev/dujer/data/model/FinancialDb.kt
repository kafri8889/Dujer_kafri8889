package com.anafthdev.dujer.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.model.Currency
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity(tableName = "financial_table")
@Parcelize
data class FinancialDb(
	@PrimaryKey @ColumnInfo(name = "financial_id") val id: Int,
	@ColumnInfo(name = "financial_name") var name: String,
	@ColumnInfo(name = "financial_amount") var amount: Double,
	@ColumnInfo(name = "financial_type") var type: @RawValue FinancialType,
	@ColumnInfo(name = "financial_walletID") var walletID: Int,
	@ColumnInfo(name = "financial_categoryID") var categoryID: Int,
	@ColumnInfo(name = "financial_currency") var currency: Currency,
	@ColumnInfo(name = "financial_dateCreated") var dateCreated: Long,
): Parcelable {
	override fun hashCode(): Int {
		return super.hashCode()
			.plus(id)
			.plus(amount.toInt())
			.plus(walletID)
			.plus(categoryID)
			.plus(dateCreated.toInt())
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as FinancialDb
		
		if (id != other.id) return false
		if (name != other.name) return false
		if (amount != other.amount) return false
		if (type != other.type) return false
		if (walletID != other.walletID) return false
		if (categoryID != other.categoryID) return false
		if (currency != other.currency) return false
		if (dateCreated != other.dateCreated) return false
		
		return true
	}
}
