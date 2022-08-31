package com.anafthdev.dujer.feature.statistic.di

import com.anafthdev.dujer.feature.statistic.environment.IStatisticEnvironment
import com.anafthdev.dujer.feature.statistic.environment.StatisticEnvironment
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
