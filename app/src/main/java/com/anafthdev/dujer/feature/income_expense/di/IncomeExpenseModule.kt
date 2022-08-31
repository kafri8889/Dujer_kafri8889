package com.anafthdev.dujer.feature.income_expense.di

import com.anafthdev.dujer.feature.income_expense.environment.IIncomeExpenseEnvironment
import com.anafthdev.dujer.feature.income_expense.environment.IncomeExpenseEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class IncomeExpenseModule {
	
	@Binds
	abstract fun provideEnvironment(
		incomeExpenseEnvironment: IncomeExpenseEnvironment
	): IIncomeExpenseEnvironment
	
}