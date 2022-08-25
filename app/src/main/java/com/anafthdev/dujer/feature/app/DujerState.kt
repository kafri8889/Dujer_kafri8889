package com.anafthdev.dujer.feature.app

import androidx.compose.runtime.compositionLocalOf
import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.app.data.DujerController
import com.anafthdev.dujer.feature.app.data.UndoType
import com.anafthdev.dujer.foundation.common.BaseEffect
import com.anafthdev.dujer.model.Currency

val LocalDujerState = compositionLocalOf { DujerState() }

data class DujerState(
	val currentCurrency: Currency = Currency.DOLLAR,
	val dataCanReturned: UndoType = UndoType.Financial,
	val allBudget: List<Budget> = emptyList(),
	val allWallet: List<Wallet> = emptyList(),
	val allCategory: List<Category> = emptyList(),
	val allIncomeTransaction: List<Financial> = emptyList(),
	val allExpenseTransaction: List<Financial> = emptyList(),
	val allTransaction: List<Financial> = emptyList(),
	val controller: DujerController = object : DujerController {
		override fun sendEffect(effect: BaseEffect) {}
	}
)
