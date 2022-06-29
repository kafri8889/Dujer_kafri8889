package com.anafthdev.dujer.data.db.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.dujer.R
import com.anafthdev.dujer.model.CategoryTint

/**
 * to get balance => initialBalance + totalIncome - totalExpense
 */
@Entity(tableName = "wallet_table")
data class Wallet(
	@PrimaryKey val id: Int,
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "initialBalance") var initialBalance: Double,
	@ColumnInfo(name = "balance") var balance: Double,
	@ColumnInfo(name = "iconID") var iconID: Int,
	@ColumnInfo(name = "tint") val tint: CategoryTint,
	@ColumnInfo(name = "defaultWallet") val defaultWallet: Boolean,
): Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readInt(),
		parcel.readString()!!,
		parcel.readDouble(),
		parcel.readDouble(),
		parcel.readInt(),
		parcel.readParcelable(CategoryTint::class.java.classLoader)!!,
		parcel.readByte() != 0.toByte()
	)
	
	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(id)
		parcel.writeString(name)
		parcel.writeDouble(initialBalance)
		parcel.writeDouble(balance)
		parcel.writeInt(iconID)
		parcel.writeParcelable(tint, flags)
		parcel.writeByte(if (defaultWallet) 1 else 0)
	}
	
	override fun describeContents(): Int {
		return 0
	}
	
	companion object CREATOR : Parcelable.Creator<Wallet> {
		override fun createFromParcel(parcel: Parcel): Wallet {
			return Wallet(parcel)
		}
		
		override fun newArray(size: Int): Array<Wallet?> {
			return arrayOfNulls(size)
		}
		
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
