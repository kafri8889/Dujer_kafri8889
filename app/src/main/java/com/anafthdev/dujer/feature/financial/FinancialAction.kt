package com.anafthdev.dujer.feature.financial

import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet

sealed class FinancialAction {
	data class Update(val financial: Financial): FinancialAction()
	data class Insert(val financial: Financial): FinancialAction()
	
	data class SetFinancialNew(val financial: Financial): FinancialAction()
	data class SetFinancialTitle(val title: String): FinancialAction()
	data class SetFinancialAmountDouble(val amount: Double): FinancialAction()
	data class SetFinancialAmount(val value: TextFieldValue): FinancialAction()
	data class SetFinancialDate(val date: Long): FinancialAction()
	data class SetFinancialCategoryForIncome(val category: Category): FinancialAction()
	data class SetFinancialCategoryForExpense(val category: Category): FinancialAction()
	data class SetFinancialType(val type: FinancialType): FinancialAction()
	data class SetSelectedWallet(val wallet: Wallet): FinancialAction()
	
	data class ResetState(val currencyCode: String = ""): FinancialAction()
}
