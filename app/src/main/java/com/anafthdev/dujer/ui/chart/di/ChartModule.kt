package com.anafthdev.dujer.ui.chart.di

import com.anafthdev.dujer.ui.chart.environment.ChartEnvironment
import com.anafthdev.dujer.ui.chart.environment.IChartEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ChartModule {
	
	@Binds
	abstract fun provideEnvironment(
		chartEnvironment: ChartEnvironment
	): IChartEnvironment
	
}