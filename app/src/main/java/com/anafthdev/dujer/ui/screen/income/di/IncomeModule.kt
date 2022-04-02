package com.anafthdev.dujer.ui.screen.income.di

import android.content.Context
import com.anafthdev.dujer.di.AppModule
import com.anafthdev.dujer.ui.screen.income.IncomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object IncomeModule {
	
	@Provides
	fun provideIncomeViewModel(
		@ApplicationContext context: Context
	): IncomeViewModel = IncomeViewModel(
		AppModule.provideAppRepository(context)
	)
	
}