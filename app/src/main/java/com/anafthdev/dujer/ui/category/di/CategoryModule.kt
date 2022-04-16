package com.anafthdev.dujer.ui.category.di

import android.content.Context
import com.anafthdev.dujer.di.AppModule
import com.anafthdev.dujer.ui.category.CategoryViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CategoryModule {
	
	@Provides
	fun provideCategoryViewModel(
		@ApplicationContext context: Context
	): CategoryViewModel = CategoryViewModel(
		AppModule.provideAppRepository(context)
	)
}