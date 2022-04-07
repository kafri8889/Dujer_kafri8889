package com.anafthdev.dujer.ui.app.di

import com.anafthdev.dujer.ui.app.DujerViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DujerModule {
	
	@Provides
	fun provideDujerViewModel(): DujerViewModel = DujerViewModel()
	
}