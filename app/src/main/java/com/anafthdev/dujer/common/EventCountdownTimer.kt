package com.anafthdev.dujer.common

import android.os.CountDownTimer
import timber.log.Timber

class EventCountdownTimer {
	
	private val TAG = "EventCountdownTimer"
	
	private var _isTimerRunning: Boolean = false
	val isTimerRunning: Boolean = _isTimerRunning
	
	private var countdownTimer: CountDownTimer? = null
	
	fun startTimer(
		millisInFuture: Long,
		onTick: (Long) -> Unit,
		onFinish: () -> Unit
	) {
		if (!_isTimerRunning) {
			_isTimerRunning = true
			
			countdownTimer = object : CountDownTimer(millisInFuture, 1000) {
				override fun onTick(millisUntilFinished: Long) {
					onTick(millisUntilFinished)
					Timber.i("millisUntilFinished: $millisUntilFinished")
				}
				
				override fun onFinish() {
					_isTimerRunning = false
					onFinish()
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
