package com.anafthdev.dujer.feature.income_expense

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.app.LocalDujerState
import com.anafthdev.dujer.feature.financial.data.FinancialAction
import com.anafthdev.dujer.feature.income_expense.component.IncomeExpenseLineChart
import com.anafthdev.dujer.feature.theme.Typography
import com.anafthdev.dujer.feature.theme.extra_large_shape
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.isExpense
import com.anafthdev.dujer.foundation.extension.isIncome
import com.anafthdev.dujer.foundation.extension.replace
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uicomponent.FilterSortFinancialPopup
import com.anafthdev.dujer.foundation.uicomponent.TopAppBar
import com.anafthdev.dujer.foundation.uicomponent.swipeableFinancialCard
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun IncomeExpenseScreen(
	navController: NavController,
	viewModel: IncomeExpenseViewModel,
	onDeleteTransaction: (Financial) -> Unit
) {
	
	val context = LocalContext.current
	val dujerState = LocalDujerState.current
	
	val state by viewModel.state.collectAsState()
	
	val sortType = state.sortType
	val groupType = state.groupType
	val filterDate = state.filterDate
	val selectedMonth = state.selectedMonth
	val transactions = state.transactions
	
	var isFilterSortFinancialPopupShowed by rememberSaveable { mutableStateOf(false) }
	
	val transactionFinancials = remember(transactions) {
		transactions.data.rawFinancials
	}
	
	val incomeTransaction = remember(transactionFinancials) {
		transactionFinancials.filter { it.type.isIncome() }
	}
	val expenseTransaction = remember(transactionFinancials) {
		transactionFinancials.filter { it.type.isExpense() }
	}
	
	val incomeBalance = remember(dujerState.allIncomeTransaction) {
		dujerState.allIncomeTransaction
			.sumOf { it.amount }
	}
	val expenseBalance = remember(dujerState.allExpenseTransaction) {
		dujerState.allExpenseTransaction
			.sumOf { it.amount }
	}
	
	val incomeLineChartEntry = remember {
		mutableStateListOf<Entry>()
	}
	val expenseLineChartEntry = remember {
		mutableStateListOf<Entry>()
	}
	
	val incomeExpenseLineDataset by rememberUpdatedState(
		newValue = LineDataSet(
			if (state.financialType.isIncome()) incomeLineChartEntry else expenseLineChartEntry,
			context.getString(
				if (state.financialType.isIncome()) R.string.income else R.string.expenses
			)
		).apply {
			lineWidth = 2.5f
			cubicIntensity = .25f
			mode = LineDataSet.Mode.CUBIC_BEZIER
			color = android.graphics.Color.parseColor("#81B2CA")
			setDrawValues(false)
			setDrawFilled(false)
			setDrawCircles(false)
			setDrawHorizontalHighlightIndicator(false)
			setCircleColor(android.graphics.Color.parseColor("#81B2CA"))
		}
	)
	
	LaunchedEffect(incomeTransaction, expenseTransaction) {
		val (incomeEntry, expenseEntry) = viewModel.calculateEntry(
			incomeList = incomeTransaction,
			expenseList = expenseTransaction
		)
		
		incomeLineChartEntry.replace(incomeEntry)
		expenseLineChartEntry.replace(expenseEntry)
	}
	
	BackHandler {
		navController.popBackStack()
	}
	
	AnimatedVisibility(
		visible = isFilterSortFinancialPopupShowed,
		enter = fadeIn(
			animationSpec = tween(400)
		),
		exit = fadeOut(
			animationSpec = tween(400)
		),
		modifier = Modifier
			.fillMaxSize()
			.zIndex(2f)
	) {
		FilterSortFinancialPopup(
			isVisible = isFilterSortFinancialPopupShowed,
			sortType = sortType,
			groupType = groupType,
			filterDate = filterDate,
			monthsSelected = selectedMonth,
			onApply = { mSelectedMonth, mSortBy, mGroupType, date ->
				viewModel.dispatch(
					IncomeExpenseAction.SetSortType(mSortBy)
				)
				
				viewModel.dispatch(
					IncomeExpenseAction.SetSelectedMonth(mSelectedMonth)
				)
				
				viewModel.dispatch(
					IncomeExpenseAction.SetGroupType(mGroupType)
				)
				
				if (date != null) {
					viewModel.dispatch(
						IncomeExpenseAction.SetFilterDate(date)
					)
				}
			},
			onClose = {
				isFilterSortFinancialPopupShowed = false
			},
			onClickOutside = {
				isFilterSortFinancialPopupShowed = false
			},
			modifier = Modifier
				.systemBarsPadding()
				.padding(vertical = 24.dpScaled)
		)
	}
	
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.systemBarsPadding()
	) {
		item {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.background)
			) {
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
						text = stringResource(
							id = if (state.financialType.isIncome()) R.string.my_income else R.string.my_spending
						),
						style = Typography.titleLarge.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.Bold,
							fontSize = Typography.titleLarge.fontSize.spScaled
						)
					)
					
					IconButton(
						onClick = {
							isFilterSortFinancialPopupShowed = true
						},
						modifier = Modifier
							.padding(end = 8.dpScaled)
							.align(Alignment.CenterEnd)
					) {
						Icon(
							imageVector = Icons.Rounded.FilterList,
							contentDescription = null
						)
					}
				}
				
				IncomeExpenseLineChart(
					dataSet = incomeExpenseLineDataset
				)
				
				Column(
					modifier = Modifier
						.padding(
							top = 8.dpScaled
						)
						.fillMaxSize()
						.clip(
							extra_large_shape.copy(
								bottomStart = CornerSize(0.dpScaled),
								bottomEnd = CornerSize(0.dpScaled)
							)
						)
						.background(Color(0xFF2C383F))
				) {
					Column(
						horizontalAlignment = Alignment.CenterHorizontally,
						modifier = Modifier
							.padding(24.dpScaled)
							.fillMaxWidth()
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.fillMaxWidth()
						) {
							Text(
								text = stringResource(
									id = if (state.financialType.isIncome()) R.string.all_income else R.string.all_expenses
								),
								style = Typography.bodyMedium.copy(
									color = Color.White,
									fontWeight = FontWeight.SemiBold,
									fontSize = Typography.bodyMedium.fontSize.spScaled
								)
							)
							
							Spacer(
								modifier = Modifier
									.weight(1f)
							)
							
							Text(
								text = CurrencyFormatter.format(
									locale = deviceLocale,
									amount = if (state.financialType.isIncome()) incomeBalance else expenseBalance,
									currencyCode = LocalCurrency.current.countryCode
								),
								style = Typography.titleMedium.copy(
									color = Color.White,
									fontWeight = FontWeight.Bold,
									fontSize = Typography.titleMedium.fontSize.spScaled
								),
								modifier = Modifier
									.padding(start = 16.dpScaled)
									.horizontalScroll(
										state = rememberScrollState(),
										autoRestart = true
									)
							)
						}
					}
					
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.height(24.dpScaled)
							.clip(
								extra_large_shape.copy(
									bottomStart = CornerSize(0.dpScaled),
									bottomEnd = CornerSize(0.dpScaled)
								)
							)
							.background(MaterialTheme.colorScheme.background)
					)
				}
			}
		}
		
		swipeableFinancialCard(
			data = transactions,
			onFinancialCardDismissToEnd = { onDeleteTransaction(it) },
			onFinancialCardClicked = {
				navController.navigate(
					DujerDestination.BottomSheet.Financial.Home.createRoute(
						financialAction = FinancialAction.EDIT,
						financialID = it.id
					)
				)
			},
			onNavigateCategoryClicked = { category ->
				navController.navigate(
					DujerDestination.CategoryTransaction.Home.createRoute(category.id)
				)
			}
		)
	}
}
