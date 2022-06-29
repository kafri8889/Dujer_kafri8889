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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.common.AppUtil.toast
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.common.TextFieldCurrencyFormatter
import com.anafthdev.dujer.foundation.common.isHide
import com.anafthdev.dujer.foundation.common.keyboardAsState
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.budget.component.DeleteBudgetPopup
import com.anafthdev.dujer.ui.budget.component.ExpensesBarChart
import com.anafthdev.dujer.ui.financial.FinancialScreen
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.uicomponent.FilterSortFinancialPopup
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.uicomponent.swipeableFinancialCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
	ExperimentalMaterialApi::class
)
@Composable
fun BudgetScreen(
	budgetID: Int,
	navController: NavController,
	onTransactionCanDelete: () -> Unit,
	onDeleteTransaction: (Financial) -> Unit
) {
	
	val viewModel = hiltViewModel<BudgetViewModel>()
	
	val state by viewModel.state.collectAsState()
	
	val financial = state.financial
	
	val scope = rememberCoroutineScope()
	val financialScreenSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val hideFinancialSheetState = {
		scope.launch { financialScreenSheetState.hide() }
		Unit
	}
	
	val showFinancialSheetState = {
		scope.launch { financialScreenSheetState.show() }
		Unit
	}
	
	BackHandler {
		when {
			financialScreenSheetState.isVisible -> hideFinancialSheetState()
			else -> navController.popBackStack()
		}
	}
	
	ModalBottomSheetLayout(
		scrimColor = Color.Unspecified,
		sheetState = financialScreenSheetState,
		sheetContent = {
			FinancialScreen(
				isScreenVisible = financialScreenSheetState.isVisible,
				financial = financial,
				financialAction = FinancialAction.EDIT,
				onBack = {
					scope.launch {
						financialScreenSheetState.hide()
					}
				},
				onSave = {
					scope.launch {
						financialScreenSheetState.hide()
					}
				}
			)
		}
	) {
		BudgetScreenContent(
			budgetID = budgetID,
			navController = navController,
			viewModel = viewModel,
			state = state,
			onTransactionCanDelete = onTransactionCanDelete,
			onDeleteTransaction = onDeleteTransaction,
			onFinancialCardClicked = { financial ->
				viewModel.dispatch(
					BudgetAction.GetFinancial(financial.id)
				)
				
				showFinancialSheetState()
			}
		)
	}
	
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun BudgetScreenContent(
	budgetID: Int,
	navController: NavController,
	viewModel: BudgetViewModel,
	state: BudgetState,
	onTransactionCanDelete: () -> Unit,
	onDeleteTransaction: (Financial) -> Unit,
	onFinancialCardClicked: (Financial) -> Unit
) {
	
	val context = LocalContext.current
	val focusManager = LocalFocusManager.current
	val localCurrency = LocalCurrency.current
	val keyboardController = LocalSoftwareKeyboardController.current
	
	val keyboardState by keyboardAsState()
	
	val budget = state.budget
	val sortType = state.sortType
	val groupType = state.groupType
	val filterDate = state.filterDate
	val barEntries = state.barEntries
	val transactions = state.transactions
	val selectedMonth = state.selectedMonth
	val thisMonthExpenses = state.thisMonthExpenses
	val averagePerMonthExpense = state.averagePerMonthExpenses
	
	var isFilterSortFinancialPopupShowed by rememberSaveable { mutableStateOf(false) }
	var isDeleteBudgetPopupShowed by rememberSaveable { mutableStateOf(false) }
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
	
	AnimatedVisibility(
		visible = isDeleteBudgetPopupShowed,
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
		DeleteBudgetPopup(
			onDelete = {
				viewModel.dispatch(
					BudgetAction.DeleteBudget(budget)
				)
				
				navController.popBackStack()
			},
			onCancel = {
				isDeleteBudgetPopupShowed = false
			},
			onClickOutside = {
				isDeleteBudgetPopupShowed = false
			}
		)
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
					BudgetAction.SetSortType(mSortBy)
				)
				
				viewModel.dispatch(
					BudgetAction.SetSelectedMonth(mSelectedMonth)
				)
				
				viewModel.dispatch(
					BudgetAction.SetGroupType(mGroupType)
				)
				
				if (date != null) {
					viewModel.dispatch(
						BudgetAction.SetFilterDate(date)
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
									isDeleteBudgetPopupShowed = true
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
					
					Card(
						modifier = Modifier
							.padding(
								vertical = 8.dpScaled,
								horizontal = 16.dpScaled
							)
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.padding(
									vertical = 8.dpScaled,
									horizontal = 16.dpScaled
								)
								.fillMaxWidth()
						) {
							Text(
								text = stringResource(id = R.string.this_month),
								style = MaterialTheme.typography.titleSmall.copy(
									color = LocalUiColor.current.bodyText,
									fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.4f)
							)
							
							Spacer(modifier = Modifier.weight(0.1f))
							
							Text(
								textAlign = TextAlign.End,
								text = CurrencyFormatter.format(
									locale = deviceLocale,
									amount = thisMonthExpenses,
									currencyCode = LocalCurrency.current.countryCode
								),
								style = MaterialTheme.typography.titleSmall.copy(
									color = LocalUiColor.current.titleText,
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.5f)
							)
						}
						
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.padding(
									vertical = 8.dpScaled,
									horizontal = 16.dpScaled
								)
								.fillMaxWidth()
						) {
							Text(
								text = stringResource(id = R.string.average_per_month),
								style = MaterialTheme.typography.titleSmall.copy(
									color = LocalUiColor.current.bodyText,
									fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.4f)
							)
							
							Spacer(modifier = Modifier.weight(0.1f))
							
							Text(
								textAlign = TextAlign.End,
								text = CurrencyFormatter.format(
									locale = deviceLocale,
									amount = averagePerMonthExpense,
									currencyCode = LocalCurrency.current.countryCode
								),
								style = MaterialTheme.typography.titleSmall.copy(
									color = LocalUiColor.current.titleText,
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.5f)
							)
						}
					}
					
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
			
			swipeableFinancialCard(
				data = transactions,
				onFinancialCardCanDelete = onTransactionCanDelete,
				onFinancialCardDismissToEnd = onDeleteTransaction,
				onFinancialCardClicked = onFinancialCardClicked,
				onNavigateCategoryClicked = { category ->
					navController.navigate(
						DujerDestination.CategoryTransaction.createRoute(category.id)
					)
				}
			)
			
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
