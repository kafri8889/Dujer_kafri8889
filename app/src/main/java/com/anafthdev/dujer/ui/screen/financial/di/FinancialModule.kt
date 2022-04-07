package com.anafthdev.dujer.ui.screen.financial.di

import android.content.Context
import com.anafthdev.dujer.di.AppModule
import com.anafthdev.dujer.ui.screen.financial.FinancialViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FinancialModule {
	
	@Provides
	fun provideFinancialViewModel(
		@ApplicationContext context: Context
	): FinancialViewModel = FinancialViewModel(
		AppModule.provideAppRepository(context)
	)
	
}
