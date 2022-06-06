package com.anafthdev.dujer.foundation.common

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DelayManager(
	private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
	
	private var currentJob: Job? = null
	
	private var _isRunning = MutableStateFlow(false)
	val isRunning: StateFlow<Boolean> = _isRunning

	fun delay(
		timeInMillis: Long,
		onFinish: () -> Unit
	) {
		if (!isRunning.value) {
			currentJob = CoroutineScope(dispatcher).launch {
				_isRunning.emit(true)
				delay(timeInMillis)
				withContext(Dispatchers.Main) {
					_isRunning.emit(false)
					onFinish()
				}
			}
		}
	}
	
	fun tryCancel(): Boolean {
		return if (currentJob != null) {
			currentJob!!.cancel("canceled manually")
			true
		} else false
	}

}