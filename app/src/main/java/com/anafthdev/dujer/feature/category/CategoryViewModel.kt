package com.anafthdev.dujer.feature.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.ARG_CATEGORY_ID
import com.anafthdev.dujer.data.ARG_CATEGORY_SWIPE_ACTION
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.category.data.CategorySwipeAction
import com.anafthdev.dujer.feature.category.environment.ICategoryEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
	categoryEnvironment: ICategoryEnvironment,
	savedStateHandle: SavedStateHandle
): StatefulViewModel<CategoryState, Unit, CategoryAction, ICategoryEnvironment>(
	CategoryState(),
	categoryEnvironment
) {
	
	private val categoryID = savedStateHandle.getStateFlow(ARG_CATEGORY_ID, Category.default.id)
	private val action = savedStateHandle.getStateFlow(ARG_CATEGORY_SWIPE_ACTION, CategorySwipeAction.NOTHING)
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getCategory().collect { category ->
				setState {
					copy(
						category = category
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			categoryID.collect { id ->
				environment.setCategory(id)
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			action.collect { mAction ->
				setState {
					copy(
						swipeAction = mAction
					)
				}
			}
		}
	}
	
	override fun dispatch(action: CategoryAction) {
		when (action) {
			is CategoryAction.Edit -> {
				viewModelScope.launch {
					setState {
						copy(
							category = action.category,
							categoryName = action.category.name,
							categoryIconID = action.category.iconID,
							swipeAction = CategorySwipeAction.EDIT
						)
					}
				}
			}
			is CategoryAction.ChangeCategoryAction -> {
				viewModelScope.launch {
					setState {
						copy(
							swipeAction = action.action
						)
					}
				}
			}
			is CategoryAction.ChangeCategoryName -> {
				viewModelScope.launch {
					setState {
						copy(
							categoryName = action.name
						)
					}
				}
			}
			is CategoryAction.ChangeCategoryIconID -> {
				viewModelScope.launch {
					setState {
						copy(
							categoryIconID = action.iconID
						)
					}
				}
			}
			is CategoryAction.ChangeFinancialType -> {
				viewModelScope.launch {
					setState {
						copy(
							selectedFinancialType = action.type
						)
					}
				}
			}
			is CategoryAction.ChangeFinancialTypeForEdit -> {
				viewModelScope.launch {
					setState {
						copy(
							selectedFinancialTypeForEdit = action.type
						)
					}
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
				val category = categories.find {
					it.id == financial.category.id
				} ?: if (financial.type == FinancialType.INCOME) Category.otherIncome else Category.otherExpense
				
				financial.copy(
					category = category,
					type = category.type
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