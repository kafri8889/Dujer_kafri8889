package com.anafthdev.dujer.feature.chart.environment

import kotlinx.coroutines.CoroutineDispatcher

interface IChartEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
}