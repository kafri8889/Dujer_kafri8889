package com.anafthdev.dujer.ui.category_transaction

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.category_transaction.environment.ICategoryTransactionEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class CategoryTransactionViewModel @Inject constructor(
	categoryTransactionEnvironment: ICategoryTransactionEnvironment
): StatefulViewModel<CategoryTransactionState, Unit, CategoryTransactionAction, ICategoryTransactionEnvironment>(
	CategoryTransactionState(),
	categoryTransactionEnvironment
) {
	
	val percentDecimalFormat = DecimalFormat("###,###,##0.0")
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getCategory().collect { mCategory ->
				setState {
					copy(
						category = mCategory
					)
				}
			}
		}
	}
	
	override fun dispatch(action: CategoryTransactionAction) {
		when (action) {
			is CategoryTransactionAction.GetCategory -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setCategory(action.id)
				}
			}
		}
	}
	
}