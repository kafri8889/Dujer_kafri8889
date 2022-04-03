package com.anafthdev.dujer.foundation.common.vibrator

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object VibratorModule {
	
	@Provides
	fun provideVibratorManager(
		@ApplicationContext context: Context
	): VibratorManager = VibratorManager(context)
}