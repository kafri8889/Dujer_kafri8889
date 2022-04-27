package com.anafthdev.dujer.foundation.localized.environment

import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.preference.Language
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class LocalizedEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appDatastore: AppDatastore
): ILocalizedEnvironment {
	
	override suspend fun setLanguage(lang: Language) {
		appDatastore.setLanguage(lang)
	}
	
	override fun getLanguage(): Flow<Language> {
		return appDatastore.getLanguage
	}
	
	
}