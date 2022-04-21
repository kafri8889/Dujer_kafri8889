package com.anafthdev.dujer.ui.financial

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.combine
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
): StatefulViewModel<FinancialState, Unit, IFinancialEnvironment>(FinancialState(), financialEnvironment) {
	
	val deviceCurrency: Currency = Currency.getInstance(AppUtil.deviceLocale)
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getCategories().collect { categories ->
				setState {
					copy(
						categories = categories.combine(Category.values)
							.sortedBy { it.name }
							.distinctBy { it.id }
					)
				}
			}
			
			environment.getCurrentCurrency().collect { currency ->
				setState {
					copy(
						currentCurrency = currency
					)
				}
			}
		}
	}
	
	fun getFinancial(id: Int, action: (Financial) -> Unit) {
		viewModelScope.launch(environment.dispatcher) {
			environment.getFinancial(id, action)
		}
	}
	
	fun updateFinancial(financial: Financial, action: () -> Unit) {
		viewModelScope.launch(environment.dispatcher) {
			environment.updateFinancial(financial, action)
		}
	}
	
	fun insertFinancial(financial: Financial, action: () -> Unit) {
		viewModelScope.launch(environment.dispatcher) {
			environment.insertFinancial(financial, action)
		}
	}
	
	companion object {
		const val FINANCIAL_ACTION_NEW = "new"
		const val FINANCIAL_ACTION_EDIT = "edit"
	}
	
}