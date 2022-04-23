package com.anafthdev.dujer.foundation.uimode

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.uimode.data.UiMode
import com.anafthdev.dujer.foundation.uimode.environment.IUiModeEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UiModeViewModel @Inject constructor(
	uiModeEnvironment: IUiModeEnvironment
): StatefulViewModel<UiModeState, Unit, IUiModeEnvironment>(UiModeState(), uiModeEnvironment) {
	
	init {
		viewModelScope.launch {
			environment.getUiMode()
				.flowOn(environment.dispatcher)
				.collect { mUiMode ->
					setState {
						copy(
							uiMode = mUiMode
						)
					}
				}
		}
	}
	
	fun setUiMode(uiMode: UiMode, action: () -> Unit = {}) {
		viewModelScope.launch(environment.dispatcher) {
			environment.setUiMode(uiMode, action)
		}
	}
	
}