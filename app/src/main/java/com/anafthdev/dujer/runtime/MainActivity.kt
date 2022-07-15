package com.anafthdev.dujer.runtime

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricPrompt
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.anafthdev.dujer.BuildConfig
import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.common.AppUtil.toast
import com.anafthdev.dujer.foundation.common.BiometricManager
import com.anafthdev.dujer.foundation.common.csv.CSVWriter
import com.anafthdev.dujer.foundation.localized.LocalizedActivity
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.app.DujerApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : LocalizedActivity() {
	
	@Inject lateinit var appDatastore: AppDatastore
	
	private lateinit var biometricManager: BiometricManager
	
	val exportFinancialDataBundle = Bundle()
	
	val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
		if (result.all { it.value }) {
			val fileName = exportFinancialDataBundle.getString("fileName", "financial-export")
			val localCurrency = exportFinancialDataBundle.getParcelable("currency") ?: Currency.DOLLAR
			val wallets = exportFinancialDataBundle.getParcelableArrayList<Wallet>("wallets") ?: emptyList()
			val financials = exportFinancialDataBundle.getParcelableArrayList<Financial>("financials") ?: emptyList()
			
			CSVWriter.writeFinancial(
				this,
				fileName,
				localCurrency,
				wallets.toList(),
				financials.toList()
			)
		}
		else "Permission denied, export canceled!".toast(this)
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (BuildConfig.DEBUG) {
			Timber.plant(object : Timber.DebugTree() {
				override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
					super.log(priority, "DEBUG_$tag", message, t)
				}
			})
		}
		
		WindowCompat.setDecorFitsSystemWindows(window, false)

		lifecycleScope.launch(Dispatchers.Main) {
			val isUseBioAuthSecurity = appDatastore.isUseBioAuth.first()
			
			if (isUseBioAuthSecurity) {
				biometricManager = BiometricManager(this@MainActivity)
				biometricManager.createBiometricPrompt(
					actionSuccess = { result ->
						when (result.authenticationType) {
							BiometricPrompt.AUTHENTICATION_RESULT_TYPE_UNKNOWN -> {}
							BiometricPrompt.AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL -> {}
							BiometricPrompt.AUTHENTICATION_RESULT_TYPE_BIOMETRIC -> {}
						}
					},
					actionFailed = {
						Timber.i("Biometric: authentication failed")
					},
					actionError = { errorCode ->
						when (errorCode) {
							BiometricPrompt.ERROR_VENDOR -> Timber.i("Biometric error: ERROR_VENDOR")
							BiometricPrompt.ERROR_LOCKOUT -> Timber.i("Biometric error: ERROR_LOCKOUT")
							BiometricPrompt.ERROR_TIMEOUT -> Timber.i("Biometric error: ERROR_TIMEOUT")
							BiometricPrompt.ERROR_NO_SPACE -> Timber.i("Biometric error: ERROR_NO_SPACE")
							BiometricPrompt.ERROR_CANCELED -> Timber.i("Biometric error: ERROR_CANCELED")
							BiometricPrompt.ERROR_USER_CANCELED -> Timber.i("Biometric error: ERROR_USER_CANCELED")
							BiometricPrompt.ERROR_NO_BIOMETRICS -> Timber.i("Biometric error: ERROR_NO_BIOMETRICS")
							BiometricPrompt.ERROR_HW_NOT_PRESENT -> Timber.i("Biometric error: ERROR_HW_NOT_PRESENT")
							BiometricPrompt.ERROR_HW_UNAVAILABLE -> Timber.i("Biometric error: ERROR_HW_UNAVAILABLE")
							BiometricPrompt.ERROR_NEGATIVE_BUTTON -> Timber.i("Biometric error: ERROR_NEGATIVE_BUTTON")
							BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> Timber.i("Biometric error: ERROR_UNABLE_TO_PROCESS")
							BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> Timber.i("Biometric error: ERROR_LOCKOUT_PERMANENT")
							BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> Timber.i("Biometric error: ERROR_NO_DEVICE_CREDENTIAL")
							BiometricPrompt.ERROR_SECURITY_UPDATE_REQUIRED -> Timber.i("Biometric error: ERROR_SECURITY_UPDATE_REQUIRED")
						}
					}
				).authenticate(biometricManager.createPromptInfo())
			}
		}
		
		setContent {
			DujerApp()
		}
	}
	
}