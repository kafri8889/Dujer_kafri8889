package com.anafthdev.dujer.ui.financial.environment

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class FinancialEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_MAIN) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IFinancialEnvironment {
	
	private val _selectedFinancial = MutableStateFlow(Financial.default)
	private val selectedFinancial: StateFlow<Financial> = _selectedFinancial
	
	override fun getFinancial(): Flow<Financial> {
		return selectedFinancial
	}
	
	override suspend fun getFinancial(id: Int) {
		_selectedFinancial.emit(appRepository.get(id) ?: Financial.default)
	}
	
	override suspend fun updateFinancial(financial: Financial) {
		appRepository.update(financial)
	}
	
	override suspend fun insertFinancial(financial: Financial) {
		appRepository.insert(financial)
	}
	
}