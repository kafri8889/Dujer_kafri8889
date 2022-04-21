package com.anafthdev.dujer.ui.setting.environment

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ISettingEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun setIsUseBioAuth(useBioAuth: Boolean, action: () -> Unit = {})
	
	fun getIsUseBioAuth(): Flow<Boolean>
	
}