package com.anafthdev.dujer.ui.screen.financial.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class FinancialEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_MAIN) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IFinancialEnvironment {
	
	private val _categories = MutableLiveData(emptyList<Category>())
	private val categories: LiveData<List<Category>> = _categories
	
	override suspend fun getCategories(): Flow<List<Category>> {
		return categories.asFlow()
	}
	
	override suspend fun getFinancial(id: Int, action: (Financial) -> Unit) {
		action(appRepository.get(id))
	}
	
	override suspend fun updateFinancial(financial: Financial, action: () -> Unit) {
		appRepository.update(financial)
		action()
	}
	
	override suspend fun insertFinancial(financial: Financial, action: () -> Unit) {
		appRepository.insert(financial)
		action()
	}
	
	override suspend fun getCurrentCurrency(): Flow<Currency> {
		return appRepository.appDatastore.getCurrentCurrency
	}
}