package com.anafthdev.dujer.ui.statistic.environment

import kotlinx.coroutines.CoroutineDispatcher

interface IStatisticEnvironment {
	
	val dispatcher: CoroutineDispatcher
}