package com.anafthdev.dujer.ui.financial

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.extension.merge
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.financial.environment.IFinancialEnvironment
import com.anafthdev.dujer.util.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FinancialViewModel @Inject constructor(
	financialEnvironment: IFinancialEnvironment
): StatefulViewModel<FinancialState, Unit, FinancialAction, IFinancialEnvironment>(FinancialState(), financialEnvironment) {
	
	val deviceCurrency: Currency = Currency.getInstance(AppUtil.deviceLocale)
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getCategories().collect { categories ->
				setState {
					copy(
						categories = categories.merge(Category.values)
							.sortedBy { it.name }
							.distinctBy { it.id }
					)
				}
			}
		}
	}
	
	override fun dispatch(action: FinancialAction) {
		when (action) {
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
		}
	}
	
}