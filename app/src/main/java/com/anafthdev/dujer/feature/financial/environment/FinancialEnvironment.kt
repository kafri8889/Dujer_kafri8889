package com.anafthdev.dujer.feature.financial.environment

import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.repository.Repository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class FinancialEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val repository: Repository
): IFinancialEnvironment {
	
	private val _selectedFinancial = MutableStateFlow(Financial.default)
	private val selectedFinancial: StateFlow<Financial> = _selectedFinancial
	
	override fun getFinancial(id: Int): Flow<Financial> {
		return repository.getFinancialByID(id)
	}
	
	override suspend fun setFinancial(id: Int) {
	
	}
	
	override suspend fun updateFinancial(financial: Financial) {
		repository.updateFinancial(financial)
	}
	
	override suspend fun insertFinancial(financial: Financial) {
		repository.insertFinancial(financial)
	}
	
}