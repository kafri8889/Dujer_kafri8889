package com.anafthdev.dujer.ui.category.di

import com.anafthdev.dujer.ui.category.environment.CategoryEnvironment
import com.anafthdev.dujer.ui.category.environment.ICategoryEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CategoryModule {
	
	@Binds
	abstract fun provideEnvironment(
		categoryEnvironment: CategoryEnvironment
	): ICategoryEnvironment
}