package com.anafthdev.dujer.feature.category_transaction.environment

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.repository.Repository
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.percentOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class CategoryTransactionEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val repository: Repository
): ICategoryTransactionEnvironment {
	
	private val _lastCategoryId = MutableStateFlow(Category.default.id)
	private val lastCategoryId: StateFlow<Int> = _lastCategoryId
	
	private val _percentForCurrentCategory = MutableStateFlow(0.0)
	private val percentForCurrentCategory: StateFlow<Double> = _percentForCurrentCategory
	
	private val _category = MutableStateFlow(Category.default)
	private val category: StateFlow<Category> = _category
	
	init {
		CoroutineScope(dispatcher).launch {
			combine(
				category,
				repository.getAllCategory()
			) { lastID, categories ->
				lastID to categories
			}.collect { (category, categories) ->
				var totalAmount = 0.0
				val currentCategoryAmount = category.financials.sumOf { it.amount }
				
				categories.forEach { mCategory ->
					totalAmount += mCategory.financials.sumOf { it.amount }
//					if (mCategory.type.isIncome()) {
//						totalAmount += mCategory.financials.sumOf { it.amount }
//					} else {
//						totalAmount -= mCategory.financials.sumOf { it.amount }
//					}
				}
				
				_percentForCurrentCategory.emit(
					totalAmount.percentOf(currentCategoryAmount) { 0.0 }
				)
			}
		}
	}
	
	override fun getCategory(): Flow<Category> {
		return category
	}
	
	override fun getPercent(): Flow<Double> {
		return percentForCurrentCategory
	}
	
	override suspend fun setCategory(id: Int) {
		_category.emit(
			repository.getCategoryByID(id) ?: Category.otherIncome
		)
	}
	
}