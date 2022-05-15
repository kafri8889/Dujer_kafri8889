package com.anafthdev.dujer.ui.financial

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Wallet

data class FinancialState(
	val wallets: List<Wallet> = emptyList(),
	val categories: List<Category> = emptyList()
)
