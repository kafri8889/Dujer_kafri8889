package com.anafthdev.dujer.foundation.uimode.environment

import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.uimode.data.UiMode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class UiModeEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appDatastore: AppDatastore
): IUiModeEnvironment {
	
	override suspend fun getUiMode(): Flow<UiMode> {
		return appDatastore.getUiMode
	}
	
	override fun setUiMode(uiMode: UiMode, action: () -> Unit) {
		appDatastore.setUiMode(uiMode, action)
	}
	
}