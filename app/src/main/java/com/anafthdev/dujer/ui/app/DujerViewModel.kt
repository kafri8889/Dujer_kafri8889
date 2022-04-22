package com.anafthdev.dujer.ui.app

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.app.environment.IDujerEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DujerViewModel @Inject constructor(
	dujerEnvironment: IDujerEnvironment
): StatefulViewModel<DujerState, Unit, IDujerEnvironment>(DujerState(), dujerEnvironment) {
	
	fun vibrate(millis: Long) {
		viewModelScope.launch {
			environment.vibrate(millis)
		}
	}
	
}