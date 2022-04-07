package com.anafthdev.dujer.ui.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.ui.screen.financial.FinancialViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DujerViewModel @Inject constructor(): ViewModel() {
	
	private val _isFinancialSheetShowed = MutableLiveData(false)
	val isFinancialSheetShowed: LiveData<Boolean> = _isFinancialSheetShowed
	
	private val _financialID = MutableLiveData(Financial.default.id)
	val financialID: LiveData<Int> = _financialID
	
	private val _financialAction = MutableLiveData(FinancialViewModel.FINANCIAL_ACTION_NEW)
	val financialAction: LiveData<String> = _financialAction
	
	fun navigateToFinancialScreen(id: Int, action: String) {
		setFinancialID(id)
		setFinancialAction(action)
		showFinancialSheet()
	}
	
	fun reset() {
		setFinancialID(Financial.default.id)
		setFinancialAction(FinancialViewModel.FINANCIAL_ACTION_NEW)
		hideFinancialSheet()
	}
	
	fun setFinancialAction(action: String) {
		_financialAction.value = action
	}
	
	fun setFinancialID(id: Int) {
		_financialID.value = id
	}
	
	fun showFinancialSheet() {
		_isFinancialSheetShowed.value = true
	}
	
	fun hideFinancialSheet() {
		_isFinancialSheetShowed.value = false
	}
}