package com.anafthdev.dujer.ui.statistic.di

import com.anafthdev.dujer.ui.statistic.environment.IStatisticEnvironment
import com.anafthdev.dujer.ui.statistic.environment.StatisticEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class StatisticModule {
	
	@Binds
	abstract fun provideEnvironment(
		statisticEnvironment: StatisticEnvironment
	): IStatisticEnvironment
	
}
