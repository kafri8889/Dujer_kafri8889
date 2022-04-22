package com.anafthdev.dujer.ui.app.environment

import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.foundation.common.vibrator.VibratorManager
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class DujerEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val vibratorManager: VibratorManager,
	private val appDatastore: AppDatastore,
): IDujerEnvironment {
	
	override suspend fun getCurrentCurrency(): Flow<Currency> {
		return appDatastore.getCurrentCurrency
	}
	
	override fun vibrate(millis: Long) {
		vibratorManager.vibrate(millis)
	}
	
}