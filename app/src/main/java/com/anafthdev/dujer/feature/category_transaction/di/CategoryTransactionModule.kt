package com.anafthdev.dujer.feature.category_transaction.di

import com.anafthdev.dujer.feature.category_transaction.environment.CategoryTransactionEnvironment
import com.anafthdev.dujer.feature.category_transaction.environment.ICategoryTransactionEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CategoryTransactionModule {
	
	@Binds
	abstract fun provideEnvironment(
		categoryTransactionEnvironment: CategoryTransactionEnvironment
	): ICategoryTransactionEnvironment
	
}