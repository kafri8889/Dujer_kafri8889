package com.anafthdev.dujer.model

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.compositionLocalOf

data class Currency(
	val name: String,
	val country: String,
	val countryCode: String,
	val symbol: String
): Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!
	)
	
	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(name)
		parcel.writeString(country)
		parcel.writeString(countryCode)
		parcel.writeString(symbol)
	}
	
	override fun describeContents(): Int {
		return 0
	}
	
	companion object CREATOR : Parcelable.Creator<Currency> {
		override fun createFromParcel(parcel: Parcel): Currency {
			return Currency(parcel)
		}
		
		override fun newArray(size: Int): Array<Currency?> {
			return arrayOfNulls(size)
		}
		
		val DOLLAR = with(android.icu.util.Currency.getInstance(com.anafthdev.dujer.data.Currency.USD.name)) {
			Currency(
				name = "Dollar",
				country = displayName,
				countryCode = currencyCode,
				symbol = symbol
			)
		}
		
		val availableCurrency = arrayListOf<Currency>().apply {
			com.anafthdev.dujer.data.Currency.values().forEach { currencyID ->
				val currency = java.util.Currency.getInstance(currencyID.name)
				add(
					Currency(
						name = "",
						country = currency.displayName,
						countryCode = currency.currencyCode,
						symbol = currency.symbol
					)
				)
			}
		}.sortedBy { it.country }
	}
	
	
}

val LocalCurrency = compositionLocalOf { Currency.DOLLAR }
