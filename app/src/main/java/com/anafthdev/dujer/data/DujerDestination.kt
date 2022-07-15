package com.anafthdev.dujer.data

import com.anafthdev.dujer.ui.category.data.CategorySwipeAction

sealed class DujerDestination(val route: String) {
	
	object Dashboard: DujerDestination("dashboard")
	
	object BudgetList: DujerDestination("budgetList")
	
	object Setting: DujerDestination("setting")
	
	object Currency: DujerDestination("currency")
	
	object IncomeExpense: DujerDestination("incomeExpense/{type}") {
		fun createRoute(type: FinancialType): String {
			return "incomeExpense/${type.ordinal}"
		}
	}
	
	object Category: DujerDestination("category/{action}/{id}") {
		fun createRoute(
			action: String = CategorySwipeAction.NOTHING,
			id: Int = com.anafthdev.dujer.data.db.model.Category.default.id
		): String {
			return "category/$action/$id"
		}
	}
	
	object Wallet: DujerDestination("wallet/{id}") {
		fun createRoute(walletID: Int): String {
			return "wallet/$walletID"
		}
	}
	
	object Statistic: DujerDestination("statistic/{walletID}") {
		fun createRoute(walletID: Int): String {
			return "statistic/$walletID"
		}
	}
	
	object CategoryTransaction: DujerDestination("category_transaction/{categoryID}") {
		fun createRoute(categoryID: Int): String {
			return "category_transaction/$categoryID"
		}
	}
	
	object Budget: DujerDestination("budget/{budgetID}") {
		fun createRoute(budgetID: Int): String {
			return "budget/$budgetID"
		}
	}
	
	class BottomSheet {
		object Financial: DujerDestination("bottom-sheet/financial/{action}/{id}") {
			fun createRoute(action: String, financialID: Int): String {
				return "bottom-sheet/financial/$action/$financialID"
			}
		}
	}
	
}