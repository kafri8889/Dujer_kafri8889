package com.anafthdev.dujer.ui.app.environment

import com.anafthdev.dujer.foundation.common.vibrator.VibratorManager
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class DujerEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val vibratorManager: VibratorManager
): IDujerEnvironment {
	
	override fun vibrate(millis: Long) {
		vibratorManager.vibrate(millis)
	}
	
}