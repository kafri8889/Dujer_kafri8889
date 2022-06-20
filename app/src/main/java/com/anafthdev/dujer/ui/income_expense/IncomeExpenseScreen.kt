package com.anafthdev.dujer.ui.income_expense

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.replace
import com.anafthdev.dujer.foundation.extension.toArray
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.app.DujerAction
import com.anafthdev.dujer.ui.app.DujerViewModel
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.financial.FinancialScreen
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.income_expense.component.IncomeExpenseLineChart
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.extra_large_shape
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IncomeExpenseScreen(
	navController: NavController,
	type: FinancialType,
	dujerViewModel: DujerViewModel
) {
	
	val context = LocalContext.current
	val dujerState = LocalDujerState.current
	
	val incomeExpenseViewModel = hiltViewModel<IncomeExpenseViewModel>()
	
	val scope = rememberCoroutineScope { Dispatchers.Main }
	val financialScreenSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val state by incomeExpenseViewModel.state.collectAsState()
	
	val financial = state.financial
	
	// TODO: Filter transaksi by bulan?
	val incomeTransaction = dujerState.allIncomeTransaction
	val expenseTransaction = dujerState.allExpenseTransaction
	
	val incomeBalance = remember(incomeTransaction) {
		incomeTransaction.sumOf { it.amount }
	}
	val expenseBalance = remember(expenseTransaction) {
		expenseTransaction.sumOf { it.amount }
	}
	
	val incomeLineChartEntry = remember {
		mutableStateListOf<Entry>()
	}
	val expenseLineChartEntry = remember {
		mutableStateListOf<Entry>()
	}
	
	val hideSheet = {
		scope.launch { financialScreenSheetState.hide() }
		Unit
	}
	
	val showSheet = {
		scope.launch { financialScreenSheetState.show() }
		Unit
	}
	
	val incomeExpenseLineDataset by rememberUpdatedState(
		newValue = LineDataSet(
			if (type == FinancialType.INCOME) incomeLineChartEntry else expenseLineChartEntry,
			context.getString(
				if (type == FinancialType.INCOME) R.string.income else R.string.expenses
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
		val (incomeEntry, expenseEntry) = incomeExpenseViewModel.calculateEntry(
			incomeList = incomeTransaction,
			expenseList = expenseTransaction
		)
		
		incomeLineChartEntry.replace(incomeEntry)
		expenseLineChartEntry.replace(expenseEntry)
	}
	
	BackHandler(
		enabled = financialScreenSheetState.isVisible,
		onBack = hideSheet
	)
	
	ModalBottomSheetLayout(
		scrimColor = Color.Unspecified,
		sheetState = financialScreenSheetState,
		sheetContent = {
			FinancialScreen(
				isScreenVisible = financialScreenSheetState.isVisible,
				financial = financial,
				financialAction = FinancialAction.EDIT,
				onBack = hideSheet,
				onSave = hideSheet
			)
		}
	) {
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
								id = if (type == FinancialType.INCOME) R.string.my_income else R.string.my_spending
							),
							style = Typography.titleLarge.copy(
								color = LocalUiColor.current.titleText,
								fontWeight = FontWeight.Bold,
								fontSize = Typography.titleLarge.fontSize.spScaled
							)
						)
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
										id = if (type == FinancialType.INCOME) R.string.income_for else R.string.expenses_for,
										AppUtil.longMonths[Calendar.getInstance()[Calendar.MONTH]]
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
										amount = if (type == FinancialType.INCOME) incomeBalance else expenseBalance,
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
			
			items(
				items = if (type == FinancialType.INCOME) incomeTransaction else expenseTransaction,
				key = { item: Financial -> item.hashCode() }
			) { financial ->
				SwipeableFinancialCard(
					financial = financial,
					onDismissToEnd = {
						dujerViewModel.dispatch(
							DujerAction.DeleteFinancial(financial.toArray())
						)
					},
					onCanDelete = {
						dujerViewModel.dispatch(
							DujerAction.Vibrate(100)
						)
					},
					onClick = {
						incomeExpenseViewModel.dispatch(
							IncomeExpenseAction.SetFinancialID(financial.id)
						)
						
						showSheet()
					},
					modifier = Modifier
						.padding(horizontal = 8.dpScaled)
						.testTag("SwipeableFinancialCard")
				)
			}
		}
	}
}
