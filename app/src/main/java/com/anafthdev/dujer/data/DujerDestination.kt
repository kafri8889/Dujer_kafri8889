package com.anafthdev.dujer.data

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.anafthdev.dujer.feature.category.data.CategorySwipeAction
import com.anafthdev.dujer.feature.financial.data.FinancialAction

sealed class DujerDestination(val route: String) {
	
	class Dashboard {
		object Root: DujerDestination("dashboard/root")
		object Home: DujerDestination("dashboard/home")
	}
	
	class BudgetList {
		object Root: DujerDestination("budgetList/root")
		object Home: DujerDestination("budgetList/home")
	}
	
	class Setting: DujerDestination("setting") {
		object Root: DujerDestination("setting/root")
		object Home: DujerDestination("setting/home")
	}
	
	class ChangeCurrency: DujerDestination("currency") {
		object Root: DujerDestination("currency/root")
		object Home: DujerDestination("currency/home")
	}
	
	class IncomeExpense {
		object Root: DujerDestination("incomeExpense/root")
		object Home: DujerDestination(
			route = "incomeExpense/home?$ARG_FINANCIAL_TYPE={$ARG_FINANCIAL_TYPE}"
		) {
			fun createRoute(type: FinancialType): String {
				return "incomeExpense/home?$ARG_FINANCIAL_TYPE=${type.ordinal}"
			}
			
			val arguments = listOf(
				navArgument(ARG_FINANCIAL_TYPE) {
					defaultValue = 0
					type = NavType.IntType
				}
			)
		}
	}
	
	class Category {
		object Root: DujerDestination("category/root")
		object Home: DujerDestination(
			route = "category/home?" +
					"$ARG_CATEGORY_ID={$ARG_CATEGORY_ID}&" +
					"$ARG_CATEGORY_SWIPE_ACTION={$ARG_CATEGORY_SWIPE_ACTION}"
		) {
			fun createRoute(
				action: String = CategorySwipeAction.NOTHING,
				id: Int = com.anafthdev.dujer.data.model.Category.default.id
			): String {
				return "category/home?" +
						"$ARG_CATEGORY_ID=$id&" +
						"$ARG_CATEGORY_SWIPE_ACTION=$action"
			}
			
			val arguments = listOf(
				navArgument(ARG_CATEGORY_ID) {
					defaultValue = com.anafthdev.dujer.data.model.Category.default.id
					type = NavType.IntType
				},
				navArgument(ARG_CATEGORY_SWIPE_ACTION) {
					defaultValue = CategorySwipeAction.NOTHING
					type = NavType.StringType
				}
			)
		}
	}
	
	class Wallet {
		object Root: DujerDestination("wallet/root")
		object Home: DujerDestination(
			route = "wallet/home?$ARG_WALLET_ID={$ARG_WALLET_ID}"
		) {
			fun createRoute(walletID: Int): String {
				return "wallet/home?$ARG_WALLET_ID=$walletID"
			}
			
			val arguments = listOf(
				navArgument(ARG_WALLET_ID) {
					defaultValue = com.anafthdev.dujer.data.model.Wallet.default.id
					type = NavType.IntType
				}
			)
		}
	}
	
	class Statistic {
		object Root: DujerDestination("statistic/root")
		object Home: DujerDestination(
			route = "statistic/home?$ARG_WALLET_ID={$ARG_WALLET_ID}"
		) {
			fun createRoute(walletID: Int): String {
				return "statistic/home?$ARG_WALLET_ID=$walletID"
			}
			
			val arguments = listOf(
				navArgument(ARG_WALLET_ID) {
					defaultValue = com.anafthdev.dujer.data.model.Wallet.default.id
					type = NavType.IntType
				}
			)
		}
	}
	
	class CategoryTransaction {
		object Root: DujerDestination("category_transaction/root")
		object Home: DujerDestination(
			route = "category_transaction/home?$ARG_CATEGORY_ID={$ARG_CATEGORY_ID}"
		) {
			fun createRoute(categoryID: Int): String {
				return "category_transaction/home?$ARG_CATEGORY_ID=$categoryID"
			}
			
			val arguments = listOf(
				navArgument(ARG_CATEGORY_ID) {
					defaultValue = com.anafthdev.dujer.data.model.Category.default.id
					type = NavType.IntType
				}
			)
		}
	}
	
	class Budget {
		object Root: DujerDestination("budget/root")
		object Home: DujerDestination(
			route = "budget/home?$ARG_BUDGET_ID={$ARG_BUDGET_ID}"
		) {
			fun createRoute(budgetID: Int): String {
				return "budget/home?$ARG_BUDGET_ID=$budgetID"
			}
			
			val arguments = listOf(
				navArgument(ARG_BUDGET_ID) {
					defaultValue = com.anafthdev.dujer.data.model.Budget.defalut.id
					type = NavType.IntType
				}
			)
		}
	}
	
	class BottomSheet {
		class Financial {
			object Root: DujerDestination("bottom-sheet/financial/root")
			object Home: DujerDestination(
				route = "bottom-sheet/financial/home?" +
						"$ARG_FINANCIAL_ACTION={$ARG_FINANCIAL_ACTION}&" +
						"$ARG_FINANCIAL_ID={$ARG_FINANCIAL_ID}"
			) {
				fun createRoute(
					financialAction: String,
					financialID: Int
				): String {
					return "bottom-sheet/financial/home?" +
							"$ARG_FINANCIAL_ACTION=$financialAction&" +
							"$ARG_FINANCIAL_ID=$financialID"
				}
				
				val arguments = listOf(
					navArgument(ARG_FINANCIAL_ACTION) {
						defaultValue = FinancialAction.NEW
						type = NavType.StringType
					},
					navArgument(ARG_FINANCIAL_ID) {
						defaultValue = com.anafthdev.dujer.data.model.Financial.default.id
						type = NavType.IntType
					}
				)
			}
		}
		
		class AddWallet {
			object Root: DujerDestination("bottom-sheet/add-wallet/root")
			object Home: DujerDestination("bottom-sheet/add-wallet/home")
		}
	}
	
}

const val ARG_CATEGORY_SWIPE_ACTION = "categorySwipeAction"
const val ARG_FINANCIAL_ACTION = "financialAction"
const val ARG_FINANCIAL_TYPE = "financialType"
const val ARG_FINANCIAL_ID = "financialId"
const val ARG_CATEGORY_ID = "categoryID"
const val ARG_WALLET_ID = "walletID"
const val ARG_BUDGET_ID = "budgetID"
