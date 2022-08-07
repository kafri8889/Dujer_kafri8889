package com.anafthdev.dujer.feature.financial.di

import com.anafthdev.dujer.feature.financial.environment.FinancialEnvironment
import com.anafthdev.dujer.feature.financial.environment.IFinancialEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class FinancialModule {
	
	@Binds
	abstract fun provideEnvironment(
		financialEnvironment: FinancialEnvironment
	): IFinancialEnvironment
	
}
