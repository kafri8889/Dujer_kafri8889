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
	
	private var listener: UiModeListener? = null
	
	init {
		viewModelScope.launch {
			environment.getUiMode()
				.flowOn(environment.dispatcher)
				.collect { uiMode ->
					listener?.onChange()
					setState {
						copy(
							uiMode = uiMode
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
	
	fun setUiModeChangeListener(listener: UiModeListener) {
		this.listener = listener
	}
	
	interface UiModeListener {
		fun onChange()
	}
}