package com.anafthdev.dujer.ui.budget

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.common.isHide
import com.anafthdev.dujer.foundation.common.keyboardAsState
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.budget.component.ExpensesBarChart
import com.anafthdev.dujer.ui.budget.component.FilterSortFinancialPopup
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.util.AppUtil.toast
import com.anafthdev.dujer.util.CurrencyFormatter
import com.anafthdev.dujer.util.TextFieldCurrencyFormatter
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BudgetScreen(
	budgetID: Int,
	navController: NavController
) {
	
	val uiMode = LocalUiMode.current
	val context = LocalContext.current
	val dujerState = LocalDujerState.current
	val focusManager = LocalFocusManager.current
	val localCurrency = LocalCurrency.current
	val keyboardController = LocalSoftwareKeyboardController.current
	
	val viewModel = hiltViewModel<BudgetViewModel>()
	
	val state by viewModel.state.collectAsState()
	val keyboardState by keyboardAsState()
	
	val budget = state.budget
	val sortType = state.sortType
	val selectedMonth = state.selectedMonth
	val barEntries = state.barEntries
	val transactions = state.transactions
	
	Timber.i("sel mon: ${selectedMonth.toList()}")
	
	var isFilterSortFinancialPopupShowed by rememberSaveable { mutableStateOf(false) }
	var monthlyBudgetDouble by rememberSaveable { mutableStateOf(0.0) }
	var monthlyBudget by remember { mutableStateOf(TextFieldValue()) }
	
	LaunchedEffect(budgetID) {
		viewModel.dispatch(
			BudgetAction.GetBudget(budgetID)
		)
	}
	
	LaunchedEffect(budget.id) {
		monthlyBudgetDouble = budget.max
		monthlyBudget = TextFieldValue(
			text = CurrencyFormatter.format(
				locale = deviceLocale,
				amount = budget.max,
				useSymbol = false,
				currencyCode = localCurrency.countryCode
			)
		)
	}
	
	LaunchedEffect(keyboardState) {
		if (keyboardState.isHide()) focusManager.clearFocus(force = true)
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
			monthsSelected = selectedMonth,
			onApply = { mSelectedMonth, mSortBy ->
				viewModel.dispatch(
					BudgetAction.SetSortType(mSortBy)
				)
				
				viewModel.dispatch(
					BudgetAction.SetSelectedMonth(mSelectedMonth)
				)
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
	
	Box(
		modifier = Modifier
			.systemBarsPadding()
			.fillMaxSize()
	) {
		LazyColumn {
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
								color = LocalUiColor.current.headlineText,
								fontWeight = FontWeight.Bold,
								fontSize = Typography.titleLarge.fontSize.spScaled
							)
						)
						
						Row(
							modifier = Modifier
								.padding(end = 8.dpScaled)
								.align(Alignment.CenterEnd)
						) {
							IconButton(
								onClick = {
									isFilterSortFinancialPopupShowed = true
								}
							) {
								Icon(
									imageVector = Icons.Rounded.FilterList,
									contentDescription = null
								)
							}
							
							IconButton(
								onClick = {
								
								}
							) {
								Icon(
									painter = painterResource(id = R.drawable.ic_trash),
									contentDescription = null
								)
							}
						}
					}
					
					Text(
						text = stringResource(id = R.string.set_a_monthly_budget),
						style = MaterialTheme.typography.bodyMedium.copy(
							color = LocalUiColor.current.bodyText,
							fontWeight = FontWeight.Medium,
							fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
						),
						modifier = Modifier
							.padding(
								vertical = 8.dpScaled,
								horizontal = 16.dpScaled
							)
					)
					
					OutlinedTextField(
						maxLines = 1,
						value = monthlyBudget,
						shape = MaterialTheme.shapes.medium,
						onValueChange = { s ->
							val formattedValue = TextFieldCurrencyFormatter.getFormattedCurrency(
								fieldValue = s,
								countryCode = localCurrency.countryCode
							)
							
							monthlyBudgetDouble = formattedValue.first
							monthlyBudget = formattedValue.second
						},
						keyboardActions = KeyboardActions(
							onDone = {
								keyboardController?.hide()
							}
						),
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Number,
							imeAction = ImeAction.Done
						),
						modifier = Modifier
							.padding(
								horizontal = 16.dpScaled
							)
							.widthIn(TextFieldDefaults.MinWidth)
							.width(IntrinsicSize.Min)
					)
					
					ExpensesBarChart(
						barEntries = barEntries,
						modifier = Modifier
							.padding(
								vertical = 8.dpScaled
							)
					)
					
					Text(
						text = stringResource(id = R.string.transaction),
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.padding(16.dpScaled)
					)
				}
			}
			
			items(
				items = transactions,
				key = { item: Financial -> item.hashCode() }
			) { financial ->
				SwipeableFinancialCard(
					financial = financial,
					onCanDelete = {},
					onDismissToEnd = {  },
					onClick = {
					
					},
					modifier = Modifier
						.padding(horizontal = 12.dpScaled)
						.testTag("SwipeableFinancialCard")
				)
			}
			
			item {
				Spacer(
					modifier = Modifier.height(ButtonDefaults.MinHeight + 16.dpScaled)
				)
			}
		}
		
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center,
			modifier = Modifier
				.fillMaxWidth()
				.height(ButtonDefaults.MinHeight)
				.background(MaterialTheme.colorScheme.primary)
				.align(Alignment.BottomCenter)
				.clickable {
					if (monthlyBudgetDouble != budget.max) {
						viewModel.dispatch(
							BudgetAction.UpdateBudget(
								budget.copy(
									max = monthlyBudgetDouble
								)
							)
						)
						
						context
							.getString(R.string.changes_saved)
							.toast(context)
					}
				}
		) {
			Text(
				text = stringResource(id = R.string.save),
				style = MaterialTheme.typography.labelLarge.copy(
					color = MaterialTheme.colorScheme.onPrimary,
					fontWeight = FontWeight.Medium,
					fontSize = MaterialTheme.typography.labelLarge.fontSize.spScaled
				)
			)
		}
	}
	
}
