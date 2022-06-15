package com.anafthdev.dujer.ui.app

import androidx.compose.runtime.compositionLocalOf
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.app.data.UndoType

val LocalDujerState = compositionLocalOf { DujerState() }

data class DujerState(
	val currentCurrency: Currency = Currency.DOLLAR,
	val dataCanReturned: UndoType = UndoType.Financial,
	val allBudget: List<Budget> = emptyList(),
	val allWallet: List<Wallet> = emptyList(),
	val allCategory: List<Category> = emptyList(),
	val allIncomeTransaction: List<Financial> = emptyList(),
	val allExpenseTransaction: List<Financial> = emptyList(),
)
