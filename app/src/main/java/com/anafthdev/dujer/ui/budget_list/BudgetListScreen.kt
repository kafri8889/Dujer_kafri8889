package com.anafthdev.dujer.ui.budget_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.foundation.common.DelayManager
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.budget_list.component.BudgetCard
import com.anafthdev.dujer.ui.budget_list.subscreen.AddBudgetScreen
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.uicomponent.TopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BudgetListScreen(
	navController: NavController
) {
	
	val dujerState = LocalDujerState.current
	
	val viewModel = hiltViewModel<BudgetListViewModel>()
	
	val state by viewModel.state.collectAsState()
	
	val allBudget = dujerState.allBudget
	val allExpenseTransaction = dujerState.allExpenseTransaction
	
	val isTopSnackbarShowed = state.isTopSnackbarShowed
	val averagePerMonthCategory = state.averagePerMonthCategory
	
	val scope = rememberCoroutineScope()
	val addBudgetScreenSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val delayManager = remember {
		DelayManager()
	}
	
	val hideAddBudgetScreenSheetState = {
		scope.launch {
			addBudgetScreenSheetState.hide()
		}
		Unit
	}
	
	val showAddBudgetScreenSheetState = {
		scope.launch {
			addBudgetScreenSheetState.show()
		}
		Unit
	}
	
	BackHandler {
		when {
			addBudgetScreenSheetState.isVisible -> hideAddBudgetScreenSheetState()
			else -> navController.popBackStack()
		}
	}
	
	ModalBottomSheetLayout(
		scrimColor = Color.Unspecified,
		sheetState = addBudgetScreenSheetState,
		sheetContent = {
			AddBudgetScreen(
				isScreenVisible = addBudgetScreenSheetState.isVisible,
				showTopSnackbar = isTopSnackbarShowed,
				averagePerMonthCategory = averagePerMonthCategory,
				onBack = hideAddBudgetScreenSheetState,
				onAdded = { budget ->
					viewModel.dispatch(
						BudgetListAction.InsertBudget(budget)
					)
					
					hideAddBudgetScreenSheetState()
				},
				onBudgetExists = { isExists ->
					if (isExists) {
						viewModel.dispatch(BudgetListAction.ShowTopSnackbar)
						delayManager.delay(3000) {
							viewModel.dispatch(BudgetListAction.HideTopSnackbar)
						}
					} else viewModel.dispatch(BudgetListAction.HideTopSnackbar)
				}
			)
		}
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			FloatingActionButton(
				onClick = showAddBudgetScreenSheetState,
				modifier = Modifier
					.padding(32.dpScaled)
					.align(Alignment.BottomEnd)
					.zIndex(2f)
			) {
				Icon(
					imageVector = Icons.Rounded.Add,
					contentDescription = null,
				)
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
				
				items(
					items = allBudget,
					key = { item: Budget -> item.hashCode() }
				) { budget ->
					
					val expensesAmount = remember(budget, allExpenseTransaction) {
						allExpenseTransaction
							.filter { it.category.id == budget.category.id }
							.sumOf { it.amount }
					}
					
					BudgetCard(
						budget = budget,
						expensesAmount = expensesAmount,
						onClick = {
							navController.navigate(
								DujerDestination.Budget.createRoute(budget.id)
							) {
								launchSingleTop = true
							}
						},
						modifier = Modifier
							.padding(
								vertical = 8.dpScaled,
								horizontal = 16.dpScaled
							)
					)
				}
				
				item {
					// fab size: 56 dp, fab bottom padding: 24 dp, card to fab padding: 16 dp = 96 dp
					Spacer(
						modifier = Modifier
							.fillMaxWidth()
							.height(96.dpScaled)
					)
				}
			}
		}
	}
}
