package com.anafthdev.dujer.ui.budget.environment

import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class BudgetEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IBudgetEnvironment {
	
	private val _selectedBudget = MutableStateFlow(Budget.defalut)
	private val selectedBudget: StateFlow<Budget> = _selectedBudget
	
	override fun getBudget(): Flow<Budget> {
		return selectedBudget
	}
	
	override suspend fun setBudget(id: Int) {
		_selectedBudget.emit(
			appRepository.budgetRepository.get(id) ?: Budget.defalut
		)
	}
	
	
}