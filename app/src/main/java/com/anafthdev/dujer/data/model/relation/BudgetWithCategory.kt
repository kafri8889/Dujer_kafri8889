package com.anafthdev.dujer.data.model.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.anafthdev.dujer.data.model.BudgetDb
import com.anafthdev.dujer.data.model.CategoryDb

data class BudgetWithCategory(
	@Embedded val budgetDb: BudgetDb,
	
	@Relation(
		entity = CategoryDb::class,
		parentColumn = "budget_categoryID",
		entityColumn = "category_id"
	)
	val categoryWithFinancial: CategoryWithFinancial
)
