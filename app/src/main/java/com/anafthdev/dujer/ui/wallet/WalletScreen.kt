package com.anafthdev.dujer.ui.wallet

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowLeft
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.statistic.component.FinancialStatisticChart
import com.anafthdev.dujer.ui.statistic.data.PercentValueFormatter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.expenseColor
import com.anafthdev.dujer.ui.theme.incomeColor
import com.anafthdev.dujer.ui.theme.medium_shape
import com.anafthdev.dujer.ui.wallet.component.DeleteWalletPopup
import com.anafthdev.dujer.ui.wallet.subscreen.EditBalanceBottomSheet
import com.anafthdev.dujer.ui.wallet.subscreen.FinancialBottomSheetWalletScreen
import com.anafthdev.dujer.ui.wallet.subscreen.SelectWalletBottomSheet
import com.anafthdev.dujer.uicomponent.FinancialTypeSelector
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.util.ColorTemplate
import com.anafthdev.dujer.util.CurrencyFormatter
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
	walletID: Int,
	navController: NavController,
	onTransactionCanDelete: () -> Unit,
	onDeleteTransaction: (Financial) -> Unit
) {
	
	val dujerState = LocalDujerState.current
	
	val walletViewModel = hiltViewModel<WalletViewModel>()
	
	val state by walletViewModel.state.collectAsState()
	
	val wallet = state.wallet
	val wallets = state.wallets
	val financial = state.financial
	
	val scope = rememberCoroutineScope()
	
	val editBalanceSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val selectWalletSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val financialScreenSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	var isDeleteConfirmationPopupShowing by rememberSaveable { mutableStateOf(false) }
	
	val incomeTransaction = remember(dujerState.allIncomeTransaction) {
		dujerState.allIncomeTransaction.filter { it.walletID == wallet.id }
	}
	val expenseTransaction = remember(dujerState.allExpenseTransaction) {
		dujerState.allExpenseTransaction.filter { it.walletID == wallet.id }
	}
	
	val incomeAmount = remember(dujerState.allIncomeTransaction) {
		dujerState.allIncomeTransaction.filter { it.walletID == walletID }.sumOf { it.amount }
	}
	val expenseAmount = remember(dujerState.allExpenseTransaction) {
		dujerState.allExpenseTransaction.filter { it.walletID == walletID }.sumOf { it.amount }
	}
	
	val hideEditBalanceSheetState = {
		scope.launch { editBalanceSheetState.hide() }
		Unit
	}
	
	val hideSelectWalletSheetState = {
		scope.launch { selectWalletSheetState.hide() }
		Unit
	}
	
	val hideFinancialSheetState = {
		scope.launch { financialScreenSheetState.hide() }
		Unit
	}
	
	val showEditBalanceSheetState = {
		scope.launch { editBalanceSheetState.show() }
		Unit
	}
	
	val showSelectWalletSheetState = {
		scope.launch { selectWalletSheetState.show() }
		Unit
	}
	
	val showFinancialSheetState = {
		scope.launch { financialScreenSheetState.show() }
		Unit
	}
	
	BackHandler {
		navController.popBackStack()
	}
	
	LaunchedEffect(walletID) {
		walletViewModel.dispatch(
			WalletAction.GetWallet(walletID)
		)
	}
	
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
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
					walletViewModel.dispatch(
						WalletAction.DeleteWallet(
							wallet
						)
					)
					
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
		
		FinancialBottomSheetWalletScreen(
			state = financialScreenSheetState,
			financial = financial,
			onBack = hideFinancialSheetState,
			onSave = hideFinancialSheetState
		) {
			SelectWalletBottomSheet(
				state = selectWalletSheetState,
				wallet = wallet,
				wallets = wallets,
				onWalletSelected = { wallet ->
					hideSelectWalletSheetState()
					walletViewModel.dispatch(
						WalletAction.GetWallet(wallet.id)
					)
				}
			) {
				EditBalanceBottomSheet(
					state = editBalanceSheetState,
					wallet = wallet,
					onCancel = hideEditBalanceSheetState,
					onSave = { mWallet, financial ->
						walletViewModel.dispatch(
							WalletAction.UpdateWallet(
								mWallet.copy(
									balance = (((mWallet.initialBalance + incomeAmount) - expenseAmount)).also {
										Timber.i("$it, w: ${mWallet.initialBalance}, i: $incomeAmount, e: $expenseAmount")
									}
								).also { Timber.i("$it, in: $incomeTransaction, ex: $expenseTransaction") }
							)
						)
						
						if (financial.id != Financial.default.id) {
							walletViewModel.dispatch(
								WalletAction.InsertFinancial(financial)
							)
						}
						
						hideEditBalanceSheetState()
					}
				) {
					WalletScreenContent(
						state = state,
						navController = navController,
						walletViewModel = walletViewModel,
						incomeTransaction = incomeTransaction,
						expenseTransaction = expenseTransaction,
						onTransactionCanDelete = onTransactionCanDelete,
						onDeleteTransaction = onDeleteTransaction,
						onShowFinancialSheet = showFinancialSheetState,
						onShowEditBalanceSheet = showEditBalanceSheetState,
						onShowSelectWalletSheet = showSelectWalletSheetState,
						onDeleteWallet = {
							isDeleteConfirmationPopupShowing = true
						},
					)
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WalletScreenContent(
	state: WalletState,
	navController: NavController,
	walletViewModel: WalletViewModel,
	incomeTransaction: List<Financial>,
	expenseTransaction: List<Financial>,
	onDeleteWallet: () -> Unit,
	onShowFinancialSheet: () ->Unit,
	onShowEditBalanceSheet: () ->Unit,
	onShowSelectWalletSheet: () ->Unit,
	onTransactionCanDelete: () -> Unit,
	onDeleteTransaction: (Financial) -> Unit
) {
	
	val context = LocalContext.current
	val localCurrency = LocalCurrency.current
	
	val wallet = state.wallet
	val sortType = state.sortType
	val pieEntries = state.pieEntries
	val availableCategory = state.availableCategory
	val selectedFinancialType = state.selectedFinancialType
	
	val pieColors = remember { mutableStateListOf<Int>() }
	var selectedPieColor by remember { mutableStateOf(Color.Transparent) }
	
	var selectedCategory = rememberSaveable(saver = Category.Saver) { Category.default }
	var isDataSetEmpty by rememberSaveable { mutableStateOf(false) }

	val balance = remember(wallet.balance) {
		CurrencyFormatter.format(
			locale = deviceLocale,
			amount = wallet.balance,
			useSymbol = true,
			currencyCode = localCurrency.countryCode
		)
	}
	val initialBalance = remember(wallet.initialBalance) {
		CurrencyFormatter.format(
			locale = deviceLocale,
			amount = wallet.initialBalance,
			useSymbol = true,
			currencyCode = localCurrency.countryCode
		)
	}
	val financialList = remember(
		sortType,
		incomeTransaction,
		expenseTransaction,
		selectedFinancialType
	) {
		val list = when (selectedFinancialType) {
			FinancialType.INCOME -> incomeTransaction
			FinancialType.EXPENSE -> expenseTransaction
			else -> incomeTransaction + expenseTransaction
		}
		
		return@remember when (sortType) {
			SortType.HIGHEST -> list.sortedByDescending { it.amount }
			SortType.LOWEST -> list.sortedBy { it.amount }
			else -> list.sortedBy { it.dateCreated }
		}
	}
	
	val categories = remember(availableCategory) { availableCategory }
	val financialsForSelectedCategory = remember(financialList, selectedCategory) {
		financialList.filter { it.category.id == selectedCategory.id }
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
	
	LaunchedEffect(financialList) {
		walletViewModel.dispatch(
			WalletAction.GetWallet(wallet.id)
		)
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
									DujerDestination.Statistic.createRoute(wallet.id)
								)
							},
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_chart),
								contentDescription = null
							)
						}
						
						if (!wallet.defaultWallet) {
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
						.background(wallet.tint.backgroundTint.toColor())
						.align(Alignment.CenterHorizontally)
				) {
					Icon(
						painter = painterResource(id = wallet.iconID),
						tint = wallet.tint.iconTint.toColor(),
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
						text = wallet.name,
						style = MaterialTheme.typography.bodyLarge.copy(
							fontWeight = FontWeight.SemiBold,
							fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
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
					style = MaterialTheme.typography.bodyLarge.copy(
						fontWeight = FontWeight.SemiBold,
						fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
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
					onClick = onShowEditBalanceSheet,
					modifier = Modifier
						.align(Alignment.CenterHorizontally)
				) {
					Text(
						text = "Edit Balance"
					)
				}
				
				Card(
					shape = MaterialTheme.shapes.large,
					elevation = CardDefaults.cardElevation(
						defaultElevation = 1.dpScaled
					),
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
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.4f)
							)
							
							Text(
								maxLines = 1,
								textAlign = TextAlign.End,
								text = initialBalance,
								style = MaterialTheme.typography.bodyMedium.copy(
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
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
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.4f)
							)
							
							Text(
								text = "${incomeTransaction.size} ${context.getString(R.string.transaction)}",
								textAlign = TextAlign.End,
								style = MaterialTheme.typography.bodyMedium.copy(
									color = incomeColor,
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
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.4f)
							)
							
							Text(
								text = "${expenseTransaction.size} ${context.getString(R.string.transaction)}",
								textAlign = TextAlign.End,
								style = MaterialTheme.typography.bodyMedium.copy(
									color = expenseColor,
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
						walletViewModel.dispatch(
							WalletAction.SetSelectedFinancialType(type)
						)
					},
					onDoubleClick = {
						walletViewModel.dispatch(
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
					financialType = selectedFinancialType,
					financialsForSelectedCategory = financialsForSelectedCategory,
					onNothingSelected = resetPieChart,
					onPieDataSelected = { entry: PieEntry?, highlight: Highlight? ->
						val category = if (categories.isNotEmpty()) categories[highlight?.x?.toInt() ?: -1]
						else Category.default
						
						selectedCategory = category
						selectedPieColor = try {
							pieColors[highlight?.x?.toInt() ?: -1].toColor()
						} catch (e: Exception) {
							// TODO: ganti warna
							Color.Transparent
						}
						
						Timber.i("entry: $entry, highlight: $highlight")
						Timber.i("selected category: $category")
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
						style = MaterialTheme.typography.bodyLarge.copy(
							fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
						)
					)
					
					Spacer(modifier = Modifier.weight(1f))
					
					IconButton(
						onClick = {
							walletViewModel.dispatch(
								WalletAction.SetSortType(
									if (sortType == SortType.HIGHEST) SortType.LOWEST
									else SortType.HIGHEST
								)
							)
						},
						modifier = Modifier
							.padding(2.dpScaled)
					) {
						Icon(
							imageVector = Icons.Rounded.ArrowLeft,
							contentDescription = null
						)
					}
					
					Text(
						text = stringResource(
							id = if (sortType == SortType.HIGHEST) R.string.highest
							else R.string.lowest
						),
						style = MaterialTheme.typography.bodyLarge.copy(
							fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
						)
					)
				}
			}
		}
		
		items(
			items = financialList,
			key = { item: Financial -> item.hashCode() }
		) { financial ->
			SwipeableFinancialCard(
				financial = financial,
				onCanDelete = onTransactionCanDelete,
				onDismissToEnd = { onDeleteTransaction(financial) },
				onClick = {
					walletViewModel.dispatch(
						WalletAction.GetFinancial(financial.id)
					)
					
					onShowFinancialSheet()
				},
				modifier = Modifier
					.padding(horizontal = 12.dpScaled)
					.testTag("SwipeableFinancialCard")
			)
		}
	}
}
