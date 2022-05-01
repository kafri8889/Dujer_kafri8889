package com.anafthdev.dujer.ui.chart

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.chart.environment.IChartEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
	chartEnvironment: IChartEnvironment
): StatefulViewModel<ChartState, Unit, ChartAction, IChartEnvironment>(
	ChartState(),
	chartEnvironment
) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getIncomeFinancialList().collect { incomeList ->
				setState {
					copy(
						incomeFinancialList = incomeList
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getExpenseFinancialList().collect { expenseList ->
				setState {
					copy(
						expenseFinancialList = expenseList
					)
				}
			}
		}
	}
	
	override fun dispatch(action: ChartAction) {
	
	}
	
	
}