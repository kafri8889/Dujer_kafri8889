package com.anafthdev.dujer.foundation.common

import android.os.CountDownTimer
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.referentialEqualityPolicy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

val LocalEventCountdownTimer = compositionLocalOf(
	policy = referentialEqualityPolicy()
) { EventCountdownTimer() }

class EventCountdownTimer(
	private val interval: Long = 1000
) {
	
	private val TAG = "EventCountdownTimer"
	
	private var _isTimerRunning = MutableStateFlow(false)
	val isTimerRunning: StateFlow<Boolean> = _isTimerRunning
	
	private var countdownTimer: CountDownTimer? = null
	
	fun startTimer(
		millisInFuture: Long,
		onTick: (Long) -> Unit = {},
		onFinish: () -> Unit = {}
	) {
		if (!isTimerRunning.value) {
			_isTimerRunning.tryEmit(true)
			
			countdownTimer = object : CountDownTimer(millisInFuture, interval) {
				override fun onTick(millisUntilFinished: Long) {
					onTick(millisUntilFinished)
					Timber.i("millisUntilFinished: $millisUntilFinished")
				}
				
				override fun onFinish() {
					_isTimerRunning.tryEmit(false)
					onFinish()
					Timber.i("millisUntilFinished: finished!")
				}
			}
			
			countdownTimer!!.start()
		}
	}
	
	fun stop() {
		countdownTimer?.cancel()
		countdownTimer = null
	}
	
}
