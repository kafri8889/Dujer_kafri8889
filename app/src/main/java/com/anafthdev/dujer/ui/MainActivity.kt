package com.anafthdev.dujer.ui

import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import com.anafthdev.dujer.BuildConfig
import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.ui.app.DujerApp
import com.anafthdev.dujer.ui.theme.DujerTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	
	@Inject lateinit var appDatastore: AppDatastore
	
	private lateinit var telephonyManager: TelephonyManager
	
	@OptIn(ExperimentalFoundationApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (BuildConfig.DEBUG) {
			Timber.plant(object : Timber.DebugTree() {
				override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
					super.log(priority, "DEBUG_$tag", message, t)
				}
			})
		}
		
		telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
		appDatastore.setCurrentCurrency(telephonyManager.networkCountryIso) {
			Timber.i("user country id: ${telephonyManager.networkCountryIso}")
		}
		
		setContent {
			DujerTheme {
				Surface {
					CompositionLocalProvider(
						LocalOverScrollConfiguration provides null
					) {
						DujerApp()
					}
				}
			}
		}
	}
}