package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.relation.BudgetWithCategory

fun Collection<BudgetWithCategory>.toBudget(): List<Budget> {
	return map { budgetWithCategory ->
		Budget(
			id = budgetWithCategory.budgetDb.id,
			max = budgetWithCategory.budgetDb.max,
			remaining = budgetWithCategory.budgetDb.remaining,
			isMaxReached = budgetWithCategory.budgetDb.isMaxReached,
			category = budgetWithCategory.categoryWithFinancial.toCategory()
		)
	}
}

fun BudgetWithCategory.toBudget(): Budget {
	return Budget(
		id = budgetDb.id,
		max = budgetDb.max,
		remaining = budgetDb.remaining,
		isMaxReached = budgetDb.isMaxReached,
		category = categoryWithFinancial.toCategory()
	)
}
