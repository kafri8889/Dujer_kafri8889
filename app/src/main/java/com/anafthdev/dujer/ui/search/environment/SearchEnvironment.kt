package com.anafthdev.dujer.ui.search.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.AppRepository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class SearchEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: AppRepository
): ISearchEnvironment {
	
	private val _resultFinancial = MutableLiveData(emptyList<Financial>())
	private val resultFinancial: LiveData<List<Financial>> = _resultFinancial
	
	private val _resultCategory = MutableLiveData(emptyList<Category>())
	private val resultCategory: LiveData<List<Category>> = _resultCategory
	
	private var financialList: List<Financial> = emptyList()
	private var categoryList: List<Category> = emptyList()
	
	init {
		CoroutineScope(dispatcher).launch {
			appRepository.getAllFinancial().collect {
				financialList = it
			}
		}
		
		CoroutineScope(dispatcher).launch {
			appRepository.categoryRepository.getAllCategory().collect {
				categoryList = it
			}
		}
	}
	
	override suspend fun search(query: String) {
		_resultFinancial.postValue(
			financialList.filter { it.name.contains(query, true) }
		)
		
		_resultCategory.postValue(
			categoryList.filter { it.name.contains(query, true) }
		)
	}
	
	override fun getResultFinancial(): Flow<List<Financial>> {
		return resultFinancial.asFlow()
	}
	
	override fun getResultCategory(): Flow<List<Category>> {
		return resultCategory.asFlow()
	}
	
	
}