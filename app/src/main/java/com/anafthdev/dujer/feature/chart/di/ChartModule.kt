package com.anafthdev.dujer.feature.chart.di

import com.anafthdev.dujer.feature.chart.environment.ChartEnvironment
import com.anafthdev.dujer.feature.chart.environment.IChartEnvironment
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