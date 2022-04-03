package com.anafthdev.dujer.foundation.common.vibrator

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import javax.inject.Inject

class VibratorManager @Inject constructor(context: Context) {
	
//	@RequiresApi(Build.VERSION_CODES.S)
//	private val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
	
	private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
	
	fun vibrate(millis: Long) {
		when {
			Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//				vibratorManager.defaultVibrator.vibrate(
//					VibrationEffect.createOneShot(
//						millis,
//						VibrationEffect.DEFAULT_AMPLITUDE
//					)
//				)
				vibrator.vibrate(
					VibrationEffect.createOneShot(
						millis,
						VibrationEffect.DEFAULT_AMPLITUDE
					)
				)
			}
			Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
				vibrator.vibrate(
					VibrationEffect.createOneShot(
						millis,
						VibrationEffect.DEFAULT_AMPLITUDE
					)
				)
			}
			else -> vibrator.vibrate(millis)
		}
	}
}