package com.anafthdev.dujer.ui.app.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.data.repository.app.AppRepository
import com.anafthdev.dujer.foundation.common.vibrator.VibratorManager
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.app.DujerAction.DeleteCategory
import com.anafthdev.dujer.ui.app.DujerAction.DeleteFinancial
import com.anafthdev.dujer.ui.app.data.UndoType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class DujerEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val vibratorManager: VibratorManager,
	private val appRepository: AppRepository,
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
		return appRepository.budgetRepository.getAll()
	}
	
	override fun getAllWallet(): Flow<List<Wallet>> {
		return appRepository.walletRepository.getAllWallet()
	}
	
	override fun getAllCategory(): Flow<List<Category>> {
		return appRepository.categoryRepository.getAllCategory()
	}
	
	override fun getAllFinancial(): Flow<List<Financial>> {
		return appRepository.getAllFinancial()
	}
	
	override fun getCurrentCurrency(): Flow<Currency> {
		return appDatastore.getCurrentCurrency
	}
	
	override fun getDataCanBeReturned(): Flow<UndoType> {
		return dataCanReturned.asFlow()
	}
	
	override suspend fun insertWallet(vararg wallet: Wallet) {
		appRepository.walletRepository.insertWallet(*wallet)
	}
	
	override suspend fun updateWallet(vararg wallet: Wallet) {
		appRepository.walletRepository.updateWallet(*wallet)
	}
	
	override suspend fun updateFinancial(vararg financial: Financial) {
		appRepository.update(*financial)
	}
	
	override suspend fun deleteFinancial(vararg financial: Financial) {
		financialTemp.clear()
		financialTemp.addAll(financial)
		_dataCanReturned.postValue(UndoType.Financial)
		appRepository.delete(*financial)
	}
	
	override suspend fun deleteCategory(vararg category: Category) {
		categoryTemp.clear()
		categoryTemp.addAll(category)
		_dataCanReturned.postValue(UndoType.Category)
		appRepository.categoryRepository.delete(*category)
	}
	
	override suspend fun undoFinancial() {
		appRepository.insert(*financialTemp.toTypedArray())
	}
	
	override suspend fun undoCategory() {
		appRepository.categoryRepository.insert(*categoryTemp.toTypedArray())
	}
	
	override fun vibrate(millis: Long) {
		vibratorManager.vibrate(millis)
	}
	
}