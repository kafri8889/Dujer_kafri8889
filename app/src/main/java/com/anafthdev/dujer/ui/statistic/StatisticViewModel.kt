package com.anafthdev.dujer.ui.statistic

import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.statistic.environment.IStatisticEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
	statisticEnvironment: IStatisticEnvironment
): StatefulViewModel<StatisticState, Unit, StatisticAction, IStatisticEnvironment>(
	StatisticState(),
	statisticEnvironment
) {
	
	init {
	
	}
	
	override fun dispatch(action: StatisticAction) {
		when (action) {
		
		}
	}
	
}