package com.anafthdev.dujer.feature.wallet

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.app.LocalDujerState
import com.anafthdev.dujer.feature.financial.data.FinancialAction
import com.anafthdev.dujer.feature.statistic.component.FinancialStatisticChart
import com.anafthdev.dujer.feature.statistic.data.PercentValueFormatter
import com.anafthdev.dujer.feature.theme.Typography
import com.anafthdev.dujer.feature.theme.expense_color
import com.anafthdev.dujer.feature.theme.income_color
import com.anafthdev.dujer.feature.theme.medium_shape
import com.anafthdev.dujer.feature.wallet.component.DeleteWalletPopup
import com.anafthdev.dujer.feature.wallet.subscreen.SelectWalletBottomSheet
import com.anafthdev.dujer.foundation.common.ColorTemplate
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uicomponent.FilterSortFinancialPopup
import com.anafthdev.dujer.foundation.uicomponent.FinancialTypeSelector
import com.anafthdev.dujer.foundation.uicomponent.TopAppBar
import com.anafthdev.dujer.foundation.uicomponent.swipeableFinancialCard
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WalletScreen(
	navController: NavController,
	viewModel: WalletViewModel,
	onDeleteTransaction: (Financial) -> Unit,
	onDeleteWallet: (Wallet) -> Unit
) {
	
	val dujerState = LocalDujerState.current
	
	val state by viewModel.state.collectAsState()
	
	val wallet = state.wallet
	val sortType = state.sortType
	val groupType = state.groupType
	val filterDate = state.filterDate
	val selectedMonth = state.selectedMonth
	
	val wallets = dujerState.allWallet
	
	val scope = rememberCoroutineScope()
	
	val selectWalletSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	var isDeleteConfirmationPopupShowing by rememberSaveable { mutableStateOf(false) }
	var isFilterSortFinancialPopupShowed by rememberSaveable { mutableStateOf(false) }
	
	val incomeTransaction = remember(dujerState.allIncomeTransaction, wallet.id) {
		dujerState.allIncomeTransaction.filter { it.walletID == wallet.id }
	}
	val expenseTransaction = remember(dujerState.allExpenseTransaction, wallet.id) {
		dujerState.allExpenseTransaction.filter { it.walletID == wallet.id }
	}
	
	val hideSelectWalletSheetState = {
		scope.launch { selectWalletSheetState.hide() }
		Unit
	}
	
	val showSelectWalletSheetState = {
		scope.launch { selectWalletSheetState.show() }
		Unit
	}
	
	BackHandler {
		navController.popBackStack()
	}
	
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
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
				showFilterMonth = false,
				isVisible = isFilterSortFinancialPopupShowed,
				sortType = sortType,
				groupType = groupType,
				filterDate = filterDate,
				monthsSelected = selectedMonth,
				onApply = { mSelectedMonth, mSortBy, mGroupType, date ->
					viewModel.dispatch(
						WalletAction.SetSortType(mSortBy)
					)
					
					viewModel.dispatch(
						WalletAction.SetSelectedMonth(mSelectedMonth)
					)
					
					viewModel.dispatch(
						WalletAction.SetGroupType(mGroupType)
					)
					
					if (date != null) {
						viewModel.dispatch(
							WalletAction.SetFilterDate(date)
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
		
		AnimatedVisibility(
			visible = isDeleteConfirmationPopupShowing,
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
			DeleteWalletPopup(
				onDelete = {
					onDeleteWallet(state.wallet)
					navController.popBackStack()
				},
				onCancel = {
					isDeleteConfirmationPopupShowing = false
				},
				onClickOutside = {
					isDeleteConfirmationPopupShowing = false
				}
			)
		}
		
		SelectWalletBottomSheet(
			state = selectWalletSheetState,
			wallet = wallet,
			wallets = wallets,
			onWalletSelected = { wallet ->
				hideSelectWalletSheetState()
				viewModel.dispatch(
					WalletAction.GetWallet(wallet.id)
				)
			}
		) {
			WalletScreenContent(
				state = state,
				navController = navController,
				viewModel = viewModel,
				incomeTransaction = incomeTransaction,
				expenseTransaction = expenseTransaction,
				onDeleteTransaction = onDeleteTransaction,
				onShowSelectWalletSheet = showSelectWalletSheetState,
				onDeleteWallet = {
					isDeleteConfirmationPopupShowing = true
				},
				onFilterClicked = {
					isFilterSortFinancialPopupShowed = true
				}
			)
//			EditBalanceBottomSheet(
//				state = editBalanceSheetState,
//				wallet = wallet,
//				onCancel = hideEditBalanceSheetState,
//				onSave = { mWallet, financial ->
//					viewModel.dispatch(
//						WalletAction.UpdateWallet(
//							mWallet.copy(
//								balance = (((mWallet.initialBalance + incomeAmount) - expenseAmount)).also {
//									Timber.i("$it, w: ${mWallet.initialBalance}, i: $incomeAmount, e: $expenseAmount")
//								}
//							).also { Timber.i("$it, in: $incomeTransaction, ex: $expenseTransaction") }
//						)
//					)
//
//					if (financial.id != Financial.default.id) {
//						viewModel.dispatch(
//							WalletAction.InsertFinancial(financial)
//						)
//					}
//
//					hideEditBalanceSheetState()
//				}
//			) {}
		}
	}
}

@Composable
private fun WalletScreenContent(
	state: WalletState,
	navController: NavController,
	viewModel: WalletViewModel,
	incomeTransaction: List<Financial>,
	expenseTransaction: List<Financial>,
	onShowSelectWalletSheet: () ->Unit,
	onDeleteWallet: () -> Unit,
	onDeleteTransaction: (Financial) -> Unit,
	onFilterClicked: () -> Unit
) {
	
	val context = LocalContext.current
	val uiColor = LocalUiColor.current
	val dujerState = LocalDujerState.current
	val localCurrency = LocalCurrency.current
	
	val transactions = state.transactions
	val pieEntries = state.pieEntries
	val selectedFinancialType = state.selectedFinancialType
	
	val allCategory = dujerState.allCategory
	
	val pieColors = remember { mutableStateListOf<Int>() }
	var selectedPieColor by remember { mutableStateOf(Color.Transparent) }
	
	var selectedCategory = rememberSaveable(saver = Category.Saver) { Category.default }
	var isDataSetEmpty by rememberSaveable { mutableStateOf(false) }

	val balance = remember(state.wallet.balance) {
		CurrencyFormatter.format(
			locale = deviceLocale,
			amount = state.wallet.balance,
			useSymbol = true,
			currencyCode = localCurrency.countryCode
		)
	}
	val initialBalance = remember(state.wallet.initialBalance) {
		CurrencyFormatter.format(
			locale = deviceLocale,
			amount = state.wallet.initialBalance,
			useSymbol = true,
			currencyCode = localCurrency.countryCode
		)
	}
	
	val pieDataSet = remember(pieEntries) {
		val isEntryEmpty = pieEntries.isEmpty()
		val entries = pieEntries.ifEmpty { listOf(PieEntry(100f)) }
		
		isDataSetEmpty = isEntryEmpty
		
		pieColors.clear()
		pieColors.addAll(
			if (!isEntryEmpty) ColorTemplate.getRandomColor(pieEntries.size)
			else listOf(android.graphics.Color.GRAY)
		)
		
		PieDataSet(entries, "").apply {
			valueTextSize = 13f
			valueLineWidth = 2f
			selectionShift = 3f
			valueLinePart1Length = 0.6f
			valueLinePart2Length = 0.3f
			valueLinePart1OffsetPercentage = 115f  // Line starts outside of chart
			isUsingSliceColorAsValueLineColor = true
			
			colors = pieColors
			valueTextColor = uiColor.titleText.toArgb()
			valueLineColor = android.graphics.Color.MAGENTA
			yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
			valueFormatter = PercentValueFormatter()
			valueTypeface = ResourcesCompat.getFont(
				context,
				R.font.inter_regular
			)
			
			setDrawValues(!isDataSetEmpty)
		}
	}
	
	val resetPieChart = {
		selectedCategory = Category.default
		selectedPieColor = Color.Transparent
	}
	
	LaunchedEffect(pieEntries) {
		if (pieEntries.isEmpty()) {
			selectedCategory = Category.default
			selectedPieColor = Color.Transparent
		}
	}
	
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.systemBarsPadding()
	) {
		item {
			Column(
				modifier = Modifier
					.systemBarsPadding()
			) {
				TopAppBar {
					IconButton(
						onClick = {
							navController.popBackStack()
						},
						modifier = Modifier
							.align(Alignment.CenterStart)
					) {
						Icon(
							imageVector = Icons.Rounded.ArrowBack,
							contentDescription = null
						)
					}
					
					Text(
						text = stringResource(id = R.string.wallet),
						style = Typography.titleLarge.copy(
							color = LocalUiColor.current.headlineText,
							fontWeight = FontWeight.Bold,
							fontSize = Typography.titleLarge.fontSize.spScaled
						)
					)
					
					Row(
						modifier = Modifier
							.align(Alignment.CenterEnd)
					) {
						IconButton(
							onClick = {
								navController.navigate(
									DujerDestination.Statistic.Home.createRoute(state.wallet.id)
								)
							},
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_chart),
								contentDescription = null
							)
						}
						
						if (!state.wallet.defaultWallet) {
							IconButton(
								onClick = onDeleteWallet,
							) {
								Icon(
									painter = painterResource(id = R.drawable.ic_trash),
									contentDescription = null
								)
							}
						}
					}
				}
				
				Box(
					modifier = Modifier
						.padding(
							top = 16.dpScaled
						)
						.size(72.dpScaled)
						.clip(medium_shape)
						.border(
							width = 1.dpScaled,
							color = MaterialTheme.colorScheme.outline,
							shape = medium_shape
						)
						.background(state.wallet.tint.backgroundTint.toColor())
						.align(Alignment.CenterHorizontally)
				) {
					Icon(
						painter = painterResource(id = state.wallet.iconID),
						tint = state.wallet.tint.iconTint.toColor(),
						contentDescription = null,
						modifier = Modifier
							.size(28.dpScaled)
							.align(Alignment.Center)
					)
				}
				
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.padding(
							top = 8.dpScaled
						)
						.clip(MaterialTheme.shapes.medium)
						.clickable {
							onShowSelectWalletSheet()
						}
						.padding(
							vertical = 4.dpScaled,
							horizontal = 8.dpScaled
						)
						.align(Alignment.CenterHorizontally)
				) {
					Text(
						text = state.wallet.name,
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						)
					)
					
					Icon(
						imageVector = Icons.Rounded.ArrowDropDown,
						contentDescription = null,
						modifier = Modifier
							.padding(
								start = 8.dpScaled
							)
					)
				}
				
				Text(
					maxLines = 1,
					textAlign = TextAlign.Center,
					text = balance,
					style = MaterialTheme.typography.titleMedium.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.SemiBold,
						fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
					),
					modifier = Modifier
						.padding(
							vertical = 8.dpScaled,
							horizontal = 16.dpScaled
						)
						.fillMaxWidth()
						.horizontalScroll(
							autoRestart = true,
							state = rememberScrollState()
						)
				)
				
				FilledTonalButton(
					onClick = {
						navController.navigate(
							DujerDestination.BottomSheet.EditWalletBalance.Home.createRoute(
								walletID = state.wallet.id
							)
						)
					},
					modifier = Modifier
						.align(Alignment.CenterHorizontally)
				) {
					Text(
						text = "Edit Balance"
					)
				}
				
				Card(
					shape = MaterialTheme.shapes.large,
					modifier = Modifier
						.padding(
							16.dpScaled
						)
				) {
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(
								16.dpScaled
							)
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
						) {
							Text(
								text = stringResource(id = R.string.initial_balance),
								style = MaterialTheme.typography.bodyMedium.copy(
									color = LocalUiColor.current.titleText,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.4f)
							)
							
							Text(
								maxLines = 1,
								textAlign = TextAlign.End,
								text = initialBalance,
								style = MaterialTheme.typography.titleSmall.copy(
									color = LocalUiColor.current.titleText,
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.6f)
									.horizontalScroll(
										autoRestart = true,
										state = rememberScrollState()
									)
							)
						}
						
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.padding(
									top = 16.dpScaled
								)
						) {
							Text(
								text = stringResource(id = R.string.income),
								style = MaterialTheme.typography.bodyMedium.copy(
									color = LocalUiColor.current.titleText,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.4f)
							)
							
							Text(
								text = "${incomeTransaction.size} ${context.getString(R.string.transaction)}",
								textAlign = TextAlign.End,
								style = MaterialTheme.typography.bodyMedium.copy(
									color = income_color,
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.6f)
							)
						}
						
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.padding(
									top = 16.dpScaled
								)
						) {
							Text(
								text = stringResource(id = R.string.expenses),
								style = MaterialTheme.typography.bodyMedium.copy(
									color = LocalUiColor.current.titleText,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.4f)
							)
							
							Text(
								text = "${expenseTransaction.size} ${context.getString(R.string.transaction)}",
								textAlign = TextAlign.End,
								style = MaterialTheme.typography.bodyMedium.copy(
									color = expense_color,
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.6f)
							)
						}
					}
				}
				
				FinancialTypeSelector(
					selectedFinancialType = selectedFinancialType,
					onFinancialTypeChanged = { type ->
						resetPieChart()
						viewModel.dispatch(
							WalletAction.SetSelectedFinancialType(type)
						)
					},
					onDoubleClick = {
						viewModel.dispatch(
							WalletAction.SetSelectedFinancialType(FinancialType.ALL)
						)
					},
					modifier = Modifier
						.padding(8.dpScaled)
				)
				
				FinancialStatisticChart(
					dataSet = pieDataSet,
					isDataSetEmpty = isDataSetEmpty,
					category = selectedCategory,
					selectedColor = selectedPieColor,
					onNothingSelected = resetPieChart,
					onStatisticCardClicked = {
						navController.navigate(
							DujerDestination.CategoryTransaction.Home.createRoute(selectedCategory.id)
						)
					},
					onPieDataSelected = { highlight: Highlight, categoryID ->
						val category = allCategory.find { it.id == categoryID } ?: Category.default
						
						selectedCategory = category
						selectedPieColor = try {
							pieColors[highlight.x.toInt()].toColor()
						} catch (e: Exception) { Color.Transparent }
					},
					modifier = Modifier
						.padding(8.dpScaled)
				)
				
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.padding(16.dpScaled)
						.fillMaxWidth()
				) {
					Text(
						text = stringResource(id = R.string.transaction),
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						)
					)
					
					Spacer(modifier = Modifier.weight(1f))
					
					IconButton(
						onClick = onFilterClicked,
						modifier = Modifier
							.padding(2.dpScaled)
					) {
						Icon(
							imageVector = Icons.Rounded.FilterList,
							contentDescription = null
						)
					}
				}
			}
		}
		
		swipeableFinancialCard(
			data = transactions,
			onFinancialCardDismissToEnd = { onDeleteTransaction(it) },
			onFinancialCardClicked = { financial ->
				navController.navigate(
					DujerDestination.BottomSheet.Financial.Home.createRoute(
						financialAction = FinancialAction.EDIT,
						financialID = financial.id
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
