package com.anafthdev.dujer.data.datastore

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.anafthdev.dujer.data.preference.Language
import com.anafthdev.dujer.data.preference.Preference
import com.anafthdev.dujer.foundation.uimode.data.UiMode
import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AppDatastore @Inject constructor(private val context: Context) {
	
	private val scope = CoroutineScope(Job() + Dispatchers.IO)
	private fun postAction(action: () -> Unit) = Handler(Looper.getMainLooper()).post { action() }
	
	fun setUserBalance(balance: Double, action: () -> Unit) {
		scope.launch {
			context.datastore.edit { preferences ->
				preferences[userBalance] = balance
			}
		}.invokeOnCompletion { postAction(action) }
	}
	
	fun setCurrentCurrency(countryID: String, action: () -> Unit) {
		scope.launch {
			context.datastore.edit { preferences ->
				preferences[currentCurrency] = countryID
			}
		}.invokeOnCompletion { postAction(action) }
	}
	
	fun setUseBioAuth(use: Boolean, action: () -> Unit) {
		scope.launch {
			context.datastore.edit { preferences ->
				preferences[useBioAuth] = use
			}
		}.invokeOnCompletion { postAction(action) }
	}
	
	fun setLanguage(lang: Language, action: () -> Unit) {
		scope.launch {
			context.datastore.edit { preferences ->
				preferences[language] = lang.ordinal
			}
		}.invokeOnCompletion { postAction(action) }
	}
	
	fun setUiMode(mUiMode: UiMode, action: () -> Unit) {
		scope.launch {
			context.datastore.edit { preferences ->
				preferences[uiMode] = mUiMode.ordinal
			}
		}.invokeOnCompletion { postAction(action) }
	}
	
	val getUserBalance: Flow<Double> = context.datastore.data.map { preferences ->
		preferences[userBalance] ?: 0.0
	}
	
	val getCurrentCurrency: Flow<Currency> = context.datastore.data.map { preferences ->
		Timber.i("cur: ${preferences[currentCurrency]}")
		with(java.util.Currency.getInstance((preferences[currentCurrency] ?: Currency.DOLLAR.countryCode))) {
			Currency(
				name = "",
				country = displayName,
				countryCode = currencyCode,
				symbol = symbol
			)
		}
	}
	
	val isUseBioAuth: Flow<Boolean> = context.datastore.data.map { preferences ->
		preferences[useBioAuth] ?: false
	}
	
	val getLanguage: Flow<Language> = context.datastore.data.map { preferences ->
		Language.values()[preferences[language] ?: Language.ENGLISH.ordinal]
	}
	
	val getUiMode: Flow<UiMode> = context.datastore.data.map { preferences ->
		UiMode.values()[preferences[uiMode] ?: UiMode.LIGHT.ordinal]
	}
	
	companion object {
		val Context.datastore: DataStore<Preferences> by preferencesDataStore("app_datastore")
		
		val userBalance = doublePreferencesKey(Preference.USER_BALANCE)
		val currentCurrency = stringPreferencesKey(Preference.CURRENT_CURRENCY)
		val useBioAuth = booleanPreferencesKey(Preference.USE_BIO_AUTH)
		val language = intPreferencesKey(Preference.LANGUAGE)
		val uiMode = intPreferencesKey(Preference.UI_MODE)
		
	}
}