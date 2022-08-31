package com.anafthdev.dujer.feature.budget.di

import com.anafthdev.dujer.feature.budget.environment.BudgetEnvironment
import com.anafthdev.dujer.feature.budget.environment.IBudgetEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class BudgetModule {
	
	@Binds
	abstract fun provideEnvironment(
		budgetEnvironment: BudgetEnvironment
	): IBudgetEnvironment
	
}