package com.anafthdev.dujer.ui.setting

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.setting.environment.ISettingEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
	settingEnvironment: ISettingEnvironment
): StatefulViewModel<SettingState, Unit, ISettingEnvironment>(SettingState(), settingEnvironment) {
	
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
	
	fun setUseBioAuth(useBioAuth: Boolean, action: () -> Unit = {}) {
		viewModelScope.launch(environment.dispatcher) {
			environment.setIsUseBioAuth(useBioAuth, action)
		}
	}
	
}