package com.anafthdev.dujer.data.db.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.model.Currency

@Entity(tableName = "financial_table")
data class Financial(
	@PrimaryKey val id: Int,
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "amount") var amount: Double,
	@ColumnInfo(name = "type") var type: FinancialType,
	@ColumnInfo(name = "walletID") var walletID: Int,
	@ColumnInfo(name = "category") var category: Category,
	@ColumnInfo(name = "currency") var currency: Currency,
	@ColumnInfo(name = "dateCreated") var dateCreated: Long,
): Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readInt(),
		parcel.readString()!!,
		parcel.readDouble(),
		FinancialType.values()[parcel.readInt()],
		parcel.readInt(),
		parcel.readParcelable(Category::class.java.classLoader)!!,
		parcel.readParcelable(Currency::class.java.classLoader)!!,
		parcel.readLong()
	)
	
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
	
	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(id)
		parcel.writeString(name)
		parcel.writeDouble(amount)
		parcel.writeInt(type.ordinal)
		parcel.writeInt(walletID)
		parcel.writeParcelable(category, flags)
		parcel.writeParcelable(currency, flags)
		parcel.writeLong(dateCreated)
	}
	
	override fun describeContents(): Int {
		return 0
	}
	
	companion object CREATOR : Parcelable.Creator<Financial> {
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
		
		override fun createFromParcel(parcel: Parcel): Financial {
			return Financial(parcel)
		}
		
		override fun newArray(size: Int): Array<Financial?> {
			return arrayOfNulls(size)
		}
	}
}
