package com.anafthdev.dujer.ui.budget

import com.anafthdev.dujer.data.db.model.Budget

data class BudgetState(
	val budget: Budget = Budget.defalut
)
