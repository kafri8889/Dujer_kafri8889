package com.anafthdev.dujer.data.datasource.local.db

import androidx.room.TypeConverter
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.model.CategoryTint
import com.anafthdev.dujer.model.Currency
import com.google.gson.Gson

object DatabaseConverter {
	
	@TypeConverter
	fun financialTypeTo(financialType: FinancialType) = financialType.ordinal
	
	@TypeConverter
	fun financialTypeFrom(ordinal: Int) = FinancialType.values()[ordinal]
	
	@TypeConverter
	fun currencyToJSON(currency: Currency) = Gson().toJson(currency)!!
	
	@TypeConverter
	fun currencyFromJSON(json: String) = Gson().fromJson(json, Currency::class.java)!!
	
	@TypeConverter
	fun categoryToJSON(category: Category) = Gson().toJson(category)!!
	
	@TypeConverter
	fun categoryFromJSON(json: String) = Gson().fromJson(json, Category::class.java)!!
	
	@TypeConverter
	fun categoryTintToJSON(tint: CategoryTint) = Gson().toJson(tint)!!
	
	@TypeConverter
	fun categoryTintFromJSON(json: String) = Gson().fromJson(json, CategoryTint::class.java)!!
	
	@TypeConverter
	fun intListToJSON(list: List<Int>) = Gson().toJson(list)!!
	
	@TypeConverter
	fun intListFromJSON(json: String) = Gson().fromJson(json, Array<Int>::class.java).toList()
	
}