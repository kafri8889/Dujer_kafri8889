package com.anafthdev.dujer.ui.financial

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.financial.environment.IFinancialEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinancialViewModel @Inject constructor(
	financialEnvironment: IFinancialEnvironment
): StatefulViewModel<FinancialState, Unit, FinancialAction, IFinancialEnvironment>(
	FinancialState(),
	financialEnvironment
) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getFinancial().collect { financial ->
				setState {
					copy(
						selectedFinancial = financial
					)
				}
			}
		}
	}
	
	override fun dispatch(action: FinancialAction) {
		when (action) {
			is FinancialAction.GetFinancial -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.getFinancial(action.id)
				}
			}
			is FinancialAction.Update -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.updateFinancial(action.financial)
				}
			}
			is FinancialAction.Insert -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.insertFinancial(action.financial)
				}
			}
			is FinancialAction.SetFinancialNew -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							financialNew = action.financial
						)
					}
				}
			}
			is FinancialAction.SetFinancialTitle -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							financialTitle = action.title
						)
					}
				}
			}
			is FinancialAction.SetFinancialAmountDouble -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							financialAmountDouble = action.amount
						)
					}
				}
			}
			is FinancialAction.SetFinancialAmount -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							financialAmount = action.value
						)
					}
				}
			}
			is FinancialAction.SetFinancialDate -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							financialDate = action.date
						)
					}
				}
			}
			is FinancialAction.SetFinancialCategoryForIncome -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							financialCategoryForIncome = action.category
						)
					}
				}
			}
			is FinancialAction.SetFinancialCategoryForExpense -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							financialCategoryForExpense = action.category
						)
					}
				}
			}
			is FinancialAction.SetFinancialType -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							financialType = action.type
						)
					}
				}
			}
			is FinancialAction.SetSelectedWallet -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							selectedWallet = action.wallet
						)
					}
				}
			}
			is FinancialAction.ResetState -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							financialNew = Financial.default,
							financialTitle = "",
							financialAmountDouble = 0.0,
							financialAmount = TextFieldValue(
								text = CurrencyFormatter.format(
									locale = deviceLocale,
									amount = 0.0,
									useSymbol = false,
									currencyCode = action.currencyCode
								)
							),
							financialDate = System.currentTimeMillis(),
							financialCategoryForIncome = Category.otherIncome,
							financialCategoryForExpense = Category.otherExpense,
							financialType = FinancialType.INCOME,
						)
					}
				}
			}
		}
	}
	
}