package com.anafthdev.dujer.ui.statistic

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.ui.statistic.component.MonthYearSelector

@Composable
fun StatisticScreen(
	walletID: Int,
	navController: NavController
) {
	
	val statisticViewModel = hiltViewModel<StatisticViewModel>()
	
	BackHandler {
		navController.popBackStack()
	}
	
	LaunchedEffect(walletID) {
		statisticViewModel.dispatch(
			StatisticAction.GetWallet(walletID)
		)
	}
	
	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		MonthYearSelector(
			onDateChanged = {
			
			},
			modifier = Modifier
				.padding(16.dpScaled)
				.align(Alignment.CenterHorizontally)
		)
	}
}
