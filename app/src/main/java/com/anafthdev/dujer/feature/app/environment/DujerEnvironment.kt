package com.anafthdev.dujer.feature.app.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.data.repository.Repository
import com.anafthdev.dujer.feature.app.DujerAction.DeleteCategory
import com.anafthdev.dujer.feature.app.DujerAction.DeleteFinancial
import com.anafthdev.dujer.feature.app.data.UndoType
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class DujerEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val repository: Repository,
	private val appDatastore: AppDatastore,
): IDujerEnvironment {
	
	private val _dataCanReturned = MutableLiveData<UndoType>(UndoType.Financial)
	private val dataCanReturned: LiveData<UndoType> = _dataCanReturned
	
	/**
	 * deleted financial from [DeleteFinancial]
	 */
	private val financialTemp: ArrayList<Financial> = arrayListOf()
	
	/**
	 * deleted financial from [DeleteCategory]
	 */
	private val categoryTemp: ArrayList<Category> = arrayListOf()
	
	override fun getAllBudget(): Flow<List<Budget>> {
		return repository.getAllBudget()
	}
	
	override fun getAllWallet(): Flow<List<Wallet>> {
		return repository.getAllWallet()
	}
	
	override fun getAllCategory(): Flow<List<Category>> {
		return repository.getAllCategory()
	}
	
	override fun getAllFinancial(): Flow<List<Financial>> {
		return repository.getAllFinancial()
	}
	
	override fun getCurrentCurrency(): Flow<Currency> {
		return appDatastore.getCurrentCurrency
	}
	
	override fun getDataCanBeReturned(): Flow<UndoType> {
		return dataCanReturned.asFlow()
	}
	
	override suspend fun updateBudget(vararg budget: Budget) {
		repository.updateBudget(*budget)
	}
	
	override suspend fun insertWallet(vararg wallet: Wallet) {
		repository.insertWallet(*wallet)
	}
	
	override suspend fun updateWallet(vararg wallet: Wallet) {
		repository.updateWallet(*wallet)
	}
	
	override suspend fun updateFinancial(vararg financial: Financial) {
		repository.updateFinancial(*financial)
	}
	
	override suspend fun deleteFinancial(vararg financial: Financial) {
		financialTemp.clear()
		financialTemp.addAll(financial)
		_dataCanReturned.postValue(UndoType.Financial)
		repository.deleteFinancial(*financial)
	}
	
	override suspend fun deleteCategory(vararg category: Category) {
		categoryTemp.clear()
		categoryTemp.addAll(category)
		_dataCanReturned.postValue(UndoType.Category)
		repository.deleteCategory(*category)
	}
	
	override suspend fun undoFinancial() {
		repository.insertFinancial(*financialTemp.toTypedArray())
	}
	
	override suspend fun undoCategory() {
		repository.insertCategory(*categoryTemp.toTypedArray())
	}
	
}