package com.anafthdev.dujer.feature.financial

import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.financial.data.FinancialAction
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale

data class FinancialState(
	val financialAction: String = FinancialAction.NEW,
	val selectedFinancial: Financial = Financial.default,
	val financialNew: Financial = Financial.default,
	val financialTitle: String = "",
	val financialAmountDouble: Double = 0.0,
	val financialAmount: TextFieldValue = TextFieldValue(
		text = CurrencyFormatter.format(
			locale = deviceLocale,
			amount = 0.0,
			useSymbol = false
		)
	),
	val financialDate: Long = System.currentTimeMillis(),
	val financialCategoryForIncome: Category = Category.otherIncome,
	val financialCategoryForExpense: Category = Category.otherExpense,
	val financialType: FinancialType = FinancialType.INCOME,
	val selectedWallet: Wallet = Wallet.cash
)
