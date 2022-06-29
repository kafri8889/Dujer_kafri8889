package com.anafthdev.dujer.ui.category

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.get
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.category.environment.ICategoryEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
	categoryEnvironment: ICategoryEnvironment
): StatefulViewModel<CategoryState, Unit, CategoryAction, ICategoryEnvironment>(CategoryState, categoryEnvironment) {
	
	override fun dispatch(action: CategoryAction) {
		when (action) {
			is CategoryAction.Get -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.get(action.id, action.action)
				}
			}
			is CategoryAction.Update -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.update(*action.categories)
				}
			}
			is CategoryAction.Insert -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.insert(*action.categories)
				}
			}
		}
	}
	
	suspend fun listenModifiedCategory(
		financialList: List<Financial>,
		categories: List<Category>
	) {
		environment.updateFinancial(
			*financialList.map { financial ->
				financial.copy(
					category = categories.get { it.id == financial.category.id } ?:
					if (financial.type == FinancialType.INCOME) Category.otherIncome else Category.otherExpense
				)
			}.toTypedArray()
		)
	}
	
	suspend fun listenDeletedCategory(
		financialList: List<Financial>,
		categoryIDs: List<Int>
	) {
		val filteredFinancial = financialList.filterNot { financial ->
			financial.category.id in categoryIDs
		}
		
		Timber.i("filtered list: $filteredFinancial")
		
		environment.updateFinancial(
			*filteredFinancial.map { financial ->
				financial.copy(
					category = if (financial.type == FinancialType.INCOME) Category.otherIncome
					else Category.otherExpense
				)
			}.toTypedArray()
		)
	}
	
}