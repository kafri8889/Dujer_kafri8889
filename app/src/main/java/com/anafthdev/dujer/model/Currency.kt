package com.anafthdev.dujer.model

data class Currency(
	val name: String,
	val country: String,
	val countryCode: String,
	val symbol: String
) {
	companion object {
		
		val INDONESIAN = Currency(
			name = "Rupiah",
			country = "Indonesia",
			countryCode = "id",
			symbol = "Rp"
		)
		
		val values = listOf(
			INDONESIAN
		)
	}
}
