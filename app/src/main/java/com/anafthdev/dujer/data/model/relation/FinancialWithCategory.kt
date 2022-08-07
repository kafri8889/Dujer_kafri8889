package com.anafthdev.dujer.data.model.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.anafthdev.dujer.data.model.CategoryDb
import com.anafthdev.dujer.data.model.FinancialDb

data class FinancialWithCategory(
	@Embedded val financialDb: FinancialDb,
	
	@Relation(
		entity = CategoryDb::class,
		parentColumn = "financial_categoryID",
		entityColumn = "category_id"
	)
	val categoryDb: CategoryDb
)
