package com.anafthdev.dujer.data.datastore

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.anafthdev.dujer.data.preference.Preference
import com.anafthdev.dujer.foundation.extension.get
import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppDatastore @Inject constructor(private val context: Context) {
	
	private val userBalance = doublePreferencesKey(Preference.USER_BALANCE)
	private val currentCurrency = stringPreferencesKey(Preference.CURRENT_CURRENCY)
	
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
	
	val getUserBalance: Flow<Double> = context.datastore.data.map {
		it[userBalance] ?: 0.0
	}
	
	val getCurrentCurrency: Flow<Currency> = context.datastore.data.map { preferences ->
		Currency.values.get {
			it.countryCode == (preferences[currentCurrency] ?: Currency.INDONESIAN.countryCode)
		}?: Currency.INDONESIAN
	}
	
	companion object {
		val Context.datastore: DataStore<Preferences> by preferencesDataStore("app_datastore")
	}
}