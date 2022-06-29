package com.anafthdev.dujer.foundation.common

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.anafthdev.dujer.R
import com.anafthdev.dujer.foundation.common.AppUtil.toast

class BiometricManager(
	private val activity: FragmentActivity
) {
	
	fun createPromptInfo(): BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
		.setTitle(activity.getString(R.string.bio_auth_title))
		.setDescription(activity.getString(R.string.bio_auth_description))
		.setConfirmationRequired(false)
		.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
		.setNegativeButtonText("Cancel")
		.build()
	
	fun createBiometricPrompt(actionSuccess: (BiometricPrompt.AuthenticationResult) -> Unit, actionFailed: () -> Unit, actionError: (Int) -> Unit): BiometricPrompt {
		val executor = ContextCompat.getMainExecutor(activity)
		
		val callback = object : BiometricPrompt.AuthenticationCallback() {
			override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
				when (errorCode) {
					BiometricPrompt.ERROR_CANCELED -> {}
					BiometricPrompt.ERROR_HW_NOT_PRESENT -> {}
					BiometricPrompt.ERROR_NEGATIVE_BUTTON -> activity.finishAffinity()
					BiometricPrompt.ERROR_USER_CANCELED -> activity.finishAffinity()
					BiometricPrompt.ERROR_TIMEOUT -> {
						activity.getString(R.string.session_time_out).toast(activity)
						activity.finishAffinity()
					}
					BiometricPrompt.ERROR_NO_BIOMETRICS -> {
						errString.toast(activity, Toast.LENGTH_LONG)
						activity.getString(R.string.add_fingerprint_first).toast(activity, Toast.LENGTH_LONG)
					}
					else -> {
						errString.toast(activity, Toast.LENGTH_LONG)
						activity.finishAffinity()
					}
				}
				
				actionError(errorCode)
				super.onAuthenticationError(errorCode, errString)
			}
			
			override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
				actionSuccess(result)
				super.onAuthenticationSucceeded(result)
			}
			
			override fun onAuthenticationFailed() {
				actionFailed()
				super.onAuthenticationFailed()
			}
		}
		
		return BiometricPrompt(activity, executor, callback)
	}
}