package com.anafthdev.dujer.feature.category_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.ARG_CATEGORY_ID
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.feature.category_transaction.environment.ICategoryTransactionEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class CategoryTransactionViewModel @Inject constructor(
	categoryTransactionEnvironment: ICategoryTransactionEnvironment,
	savedStateHandle: SavedStateHandle
): StatefulViewModel<CategoryTransactionState, Unit, CategoryTransactionAction, ICategoryTransactionEnvironment>(
	CategoryTransactionState(),
	categoryTransactionEnvironment
) {
	
	private val categoryID = savedStateHandle.getStateFlow(ARG_CATEGORY_ID, Category.default.id)
	
	val percentDecimalFormat = DecimalFormat("###,###,##0.0")
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			categoryID.collect { id ->
				environment.setCategory(id)
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getCategory().collect { mCategory ->
				setState {
					copy(
						category = mCategory
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getPercent().collect { percent ->
				setState {
					copy(
						percent = percentDecimalFormat.format(percent) + "%"
					)
				}
			}
		}
	}
	
	override fun dispatch(action: CategoryTransactionAction) {}
	
}