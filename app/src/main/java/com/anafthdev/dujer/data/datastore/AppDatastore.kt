package com.anafthdev.dujer.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.anafthdev.dujer.data.preference.Language
import com.anafthdev.dujer.data.preference.Preference
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.uimode.data.UiMode
import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDatastore @Inject constructor(private val context: Context) {
	
	suspend fun setUserBalance(balance: Double) {
		context.datastore.edit { preferences ->
			preferences[userBalance] = balance
		}
	}
	
	suspend fun setCurrentCurrency(countryID: String) {
		context.datastore.edit { preferences ->
			preferences[currentCurrency] = countryID
		}
	}
	
	suspend fun setUseBioAuth(use: Boolean) {
		context.datastore.edit { preferences ->
			preferences[useBioAuth] = use
		}
	}
	
	suspend fun setLanguage(lang: Language) {
		context.datastore.edit { preferences ->
			preferences[language] = lang.ordinal
		}
	}
	
	suspend fun setUiMode(mUiMode: UiMode) {
		context.datastore.edit { preferences ->
			preferences[uiMode] = mUiMode.ordinal
		}
	}
	
	val getUserBalance: Flow<Double> = context.datastore.data.map { preferences ->
		preferences[userBalance] ?: 0.0
	}
	
	val getCurrentCurrency: Flow<Currency> = context.datastore.data.map { preferences ->
		val code = preferences[currentCurrency]
		val currency = if (code == null) android.icu.util.Currency.getInstance(deviceLocale)
		else android.icu.util.Currency.getInstance(code)
		
		with(currency) {
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