package com.anafthdev.dujer.foundation.uimode.environment

import com.anafthdev.dujer.foundation.uimode.data.UiMode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IUiModeEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getUiMode(): Flow<UiMode>
	
	suspend fun setUiMode(uiMode: UiMode)
	
}