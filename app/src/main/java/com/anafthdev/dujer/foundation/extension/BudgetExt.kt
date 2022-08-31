package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.BudgetDb

fun Budget.toBudgetDb(): BudgetDb {
	return BudgetDb(
		id = id,
		max = max,
		categoryID = category.id,
		remaining = remaining,
		isMaxReached = isMaxReached
	)
}

fun Collection<Budget>.toBudgetDb(): List<BudgetDb> {
	return map { budget ->
		BudgetDb(
			id = budget.id,
			max = budget.max,
			categoryID = budget.category.id,
			remaining = budget.remaining,
			isMaxReached = budget.isMaxReached
		)
	}
}

fun Array<out Budget>.toBudgetDb(): Array<out BudgetDb> {
	return map { budget ->
		BudgetDb(
			id = budget.id,
			max = budget.max,
			categoryID = budget.category.id,
			remaining = budget.remaining,
			isMaxReached = budget.isMaxReached
		)
	}.toTypedArray()
}
