package com.anafthdev.dujer.ui.screen.dashboard.di

import android.content.Context
import com.anafthdev.dujer.di.AppModule
import com.anafthdev.dujer.ui.screen.dashboard.DashboardViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {
	
	@Provides
	fun provideDashboardViewModel(
		@ApplicationContext context: Context
	): DashboardViewModel = DashboardViewModel(AppModule.provideAppRepository(context))
}