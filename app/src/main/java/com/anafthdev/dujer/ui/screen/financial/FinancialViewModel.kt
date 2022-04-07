package com.anafthdev.dujer.ui.screen.financial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anafthdev.dujer.data.IAppRepository
import com.anafthdev.dujer.data.db.model.Financial
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinancialViewModel @Inject constructor(
	private val appRepository: IAppRepository
): ViewModel() {
	
	private val _financial = MutableLiveData(Financial.default)
	val financial: LiveData<Financial> = _financial
	
	fun getFinancial(id: Int) {
		_financial.value = appRepository.get(id)
	}
	
	companion object {
		const val FINANCIAL_ACTION_NEW = "new"
		const val FINANCIAL_ACTION_EDIT = "edit"
	}
	
}