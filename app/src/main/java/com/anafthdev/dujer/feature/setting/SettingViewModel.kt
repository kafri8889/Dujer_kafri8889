package com.anafthdev.dujer.feature.setting

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.feature.setting.environment.ISettingEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
	settingEnvironment: ISettingEnvironment
): StatefulViewModel<SettingState, Unit, SettingAction, ISettingEnvironment>(SettingState(), settingEnvironment) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getIsUseBioAuth().collect { isUseBioAuth ->
				setState {
					copy(
						isUseBioAuth = isUseBioAuth
					)
				}
			}
		}
	}
	
	override fun dispatch(action: SettingAction) {
		when (action) {
			is SettingAction.SetUseBioAuth -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setIsUseBioAuth(action.isUseBioAuth)
				}
			}
			is SettingAction.SetExportFinancialDataBundle -> {
				viewModelScope.launch {
					setState {
						copy(
							exportFinancialDataBundle = action.bundle
						)
					}
				}
			}
		}
	}
	
}