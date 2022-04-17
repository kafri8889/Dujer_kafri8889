package com.anafthdev.dujer.foundation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * State: A type that describes the data your feature needs to perform its logic and render its UI.
 *      The state is persistence during feature lifetime.
 * Environment: A type that holds any dependencies the feature needs, such as
 *      API clients, analytics clients, etc to decouple between UI layer and Data layer.
 */
abstract class StatefulViewModel<STATE, ENVIRONMENT>(
	initialState: STATE,
	protected val environment: ENVIRONMENT
): ViewModel() {
	
	private val _state = MutableStateFlow(initialState)
	val state: StateFlow<STATE> = _state.asStateFlow()
	
	protected suspend fun setState(newState: STATE.() -> STATE) {
		_state.emit(stateValue().newState())
	}
	
	private fun stateValue(): STATE {
		return state.value
	}

}