package com.anafthdev.dujer.ui.income_expense

import com.anafthdev.dujer.data.db.model.Financial

sealed class IncomeExpenseAction {
	data class SetFinancialID(val id: Int): IncomeExpenseAction()
	data class DeleteFinancial(val financials: Array<out Financial>): IncomeExpenseAction() {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			
			other as DeleteFinancial
			
			if (!financials.contentEquals(other.financials)) return false
			
			return true
		}
		
		override fun hashCode(): Int {
			return financials.contentHashCode()
		}
	}
}
