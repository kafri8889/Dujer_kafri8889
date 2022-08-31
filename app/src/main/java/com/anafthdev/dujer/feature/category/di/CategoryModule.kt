package com.anafthdev.dujer.feature.category.di

import com.anafthdev.dujer.feature.category.environment.CategoryEnvironment
import com.anafthdev.dujer.feature.category.environment.ICategoryEnvironment
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