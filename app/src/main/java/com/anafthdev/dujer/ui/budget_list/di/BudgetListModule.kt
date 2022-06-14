package com.anafthdev.dujer.ui.budget_list.di

import com.anafthdev.dujer.ui.budget_list.environment.BudgetListEnvironment
import com.anafthdev.dujer.ui.budget_list.environment.IBudgetListEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class BudgetListModule {
	
	@Binds
	abstract fun provideEnvironment(
		budgetEnvironment: BudgetListEnvironment
	): IBudgetListEnvironment
	
}