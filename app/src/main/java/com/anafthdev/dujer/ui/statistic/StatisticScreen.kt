package com.anafthdev.dujer.ui.statistic

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
	
	val state by statisticViewModel.state.collectAsState()
	
	val wallet = state.wallet
	val incomeTransaction = state.incomeTransaction
	val expenseTransaction = state.expenseTransaction
	
	// TODO: kalkulasi data buat pie chart
//	val groupedFinancial by remember(incomeTransaction, expenseTransaction) {
//
//	}
//
//	val pieEntry by remember(incomeTransaction, expenseTransaction) {
//
//	}
//
//	val pieDataSet by remember(pieEntry) {
//		PieDataSet(pieEntry, "")
//	}
	
	BackHandler {
		navController.popBackStack()
	}
	
	LaunchedEffect(walletID) {
		statisticViewModel.dispatch(
			StatisticAction.Get(walletID)
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
		
//		FinancialStatisticChart(
//			dataSet = ,
//			financialType = ,
//			onPieDataSelected = { entry: Entry? ->
//
//			}
//		)
	}
}
