package com.anafthdev.dujer.foundation.localized.environment

import com.anafthdev.dujer.data.preference.Language
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ILocalizedEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun setLanguage(lang: Language)
	
	fun getLanguage(): Flow<Language>
	
}