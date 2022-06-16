package com.anafthdev.dujer.ui.budget_list.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.forEachMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

class BudgetListEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IBudgetListEnvironment {
	
	private val monthFormatter = SimpleDateFormat("MMM", deviceLocale)
	
	private val _isTopSnackbarShowed = MutableStateFlow(false)
	private val isTopSnackbarShowed: StateFlow<Boolean> = _isTopSnackbarShowed
	
	private val _averagePerMonthCategory = MutableLiveData(emptyList<Pair<Double, Category>>())
	private val averagePerMonthCategory: LiveData<List<Pair<Double, Category>>> = _averagePerMonthCategory
	
	init {
		CoroutineScope(dispatcher).launch {
			combine(
				appRepository.getAllFinancial(),
				appRepository.categoryRepository.getAllCategory()
			) { financials, categories ->
				financials to categories
			}.collect { (financialList, categoryList) ->
				val categories = categoryList + Category.expenseValues
				val average = arrayListOf<Pair<Double, Category>>()
				val expenses = financialList.filter { it.type == FinancialType.EXPENSE }
				val groupedExpensesByCategory = expenses.groupBy { it.category.id }
				
				groupedExpensesByCategory.forEachMap { categoryID, mFinancials ->
					// list per month => Jan, Feb, Mar, ...
					val totalPerMonth = arrayListOf<Double>()
					
					val groupedExpensesByMonth = mFinancials.groupBy { monthFormatter.format(it.dateCreated) }
					
					groupedExpensesByMonth.forEachMap { _, nFinancials ->
						totalPerMonth.add(nFinancials.sumOf { it.amount })
					}
					
					average.add(
						totalPerMonth.average() to (categories.find { it.id == categoryID } ?: Category.default)
					)
					
					Timber.i("average: $average")
				}
				
				_averagePerMonthCategory.postValue(average)
			}
		}
	}
	
	override fun isTopSnackbarShowed(): Flow<Boolean> {
		return isTopSnackbarShowed
	}
	
	override fun getAveragePerMonthCategory(): Flow<List<Pair<Double, Category>>> {
		return averagePerMonthCategory.asFlow()
	}
	
	override suspend fun showTopSnackbar(show: Boolean) {
		_isTopSnackbarShowed.emit(show)
	}
	
	override suspend fun insertBudget(budget: Budget) {
		appRepository.budgetRepository.insert(budget)
	}
	
}