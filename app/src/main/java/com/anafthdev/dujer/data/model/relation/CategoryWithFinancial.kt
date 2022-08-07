package com.anafthdev.dujer.data.model.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.anafthdev.dujer.data.model.CategoryDb
import com.anafthdev.dujer.data.model.FinancialDb

data class CategoryWithFinancial(
	@Embedded val categoryDb: CategoryDb,
	
	@Relation(
		entity = FinancialDb::class,
		parentColumn = "category_id",
		entityColumn = "financial_categoryID"
	)
	val financialDbs: List<FinancialDb>
)
