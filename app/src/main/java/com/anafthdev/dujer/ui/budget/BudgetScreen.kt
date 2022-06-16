package com.anafthdev.dujer.ui.budget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.uicomponent.TopAppBar

@Composable
fun BudgetScreen(
	budgetID: Int,
	navController: NavController
) {
	
	val viewModel = hiltViewModel<BudgetViewModel>()
	
	val state by viewModel.state.collectAsState()
	
	LaunchedEffect(budgetID) {
		viewModel.dispatch(
			BudgetAction.GetBudget(budgetID)
		)
	}
	
	BackHandler {
		navController.popBackStack()
	}
	
	LazyColumn(
		modifier = Modifier
			.systemBarsPadding()
			.fillMaxSize()
	) {
		item {
			Column {
				TopAppBar {
					IconButton(
						onClick = {
							navController.popBackStack()
						},
						modifier = Modifier
							.padding(start = 8.dpScaled)
							.align(Alignment.CenterStart)
					) {
						Icon(
							imageVector = Icons.Rounded.ArrowBack,
							contentDescription = null
						)
					}
					
					Text(
						text = stringResource(id = R.string.budget),
						style = Typography.titleLarge.copy(
							fontWeight = FontWeight.Bold,
							fontSize = Typography.titleLarge.fontSize.spScaled
						)
					)
				}
				
				
			}
		}
	}
	
}
