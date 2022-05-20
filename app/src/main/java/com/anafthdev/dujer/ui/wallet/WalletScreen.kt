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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.darkenColor
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.merge
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.financial.FinancialScreen
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.theme.*
import com.anafthdev.dujer.ui.wallet.component.DeleteWalletPopup
import com.anafthdev.dujer.ui.wallet.subscreen.SelectWalletScreen
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.util.CurrencyFormatter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
	walletID: Int,
	navController: NavController,
	onTransactionCanDelete: () -> Unit,
	onDeleteTransaction: (Financial) -> Unit
) {
	
	val uiMode = LocalUiMode.current
	val context = LocalContext.current
	
	val walletViewModel = hiltViewModel<WalletViewModel>()
	
	val state by walletViewModel.state.collectAsState()
	
	val scope = rememberCoroutineScope()
	
	val selectWalletSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val financialScreenSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	var isDeleteConfirmationPopupShowing by remember { mutableStateOf(false) }
	
	val wallet = state.wallet
	val wallets = state.wallets
	val financial = state.financial
	val incomeTransaction = state.incomeTransaction
	val expenseTransaction = state.expenseTransaction
	
	val mixedFinancialList = remember(incomeTransaction, expenseTransaction) {
		incomeTransaction.merge(expenseTransaction).sortedBy { it.dateCreated }
	}
	
	val hideSelectWalletSheetState = {
		scope.launch { selectWalletSheetState.hide() }
		Unit
	}
	
	val showSelectWalletSheetState = {
		scope.launch { selectWalletSheetState.show() }
		Unit
	}
	
	val hideFinancialSheetState = {
		scope.launch { financialScreenSheetState.hide() }
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
		
		walletViewModel.dispatch(
			WalletAction.GetTransaction(walletID)
		)
	}
	
	LaunchedEffect(mixedFinancialList) {
		walletViewModel.dispatch(
			WalletAction.GetWallet(wallet.id)
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
		
		ModalBottomSheetLayout(
			scrimColor = Color.Unspecified,
			sheetState = financialScreenSheetState,
			sheetContent = {
				FinancialScreen(
					isScreenVisible = financialScreenSheetState.isVisible,
					financial = financial,
					financialAction = FinancialAction.EDIT,
					onBack = hideFinancialSheetState,
					onSave = hideFinancialSheetState
				)
			}
		) {
			ModalBottomSheetLayout(
				sheetState = selectWalletSheetState,
				sheetShape = RoundedCornerShape(
					topStart = shapes.medium.topStart,
					topEnd = shapes.medium.topEnd,
					bottomEnd = CornerSize(0.dpScaled),
					bottomStart = CornerSize(0.dpScaled)
				),
				sheetContent = {
					SelectWalletScreen(
						selectedWallet = wallet,
						wallets = wallets,
						onWalletSelected = { wallet ->
							hideSelectWalletSheetState()
							walletViewModel.dispatch(
								WalletAction.GetWallet(wallet.id)
							)
							
							walletViewModel.dispatch(
								WalletAction.GetTransaction(wallet.id)
							)
						}
					)
				}
			) {
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
										modifier = Modifier
									) {
										Icon(
											painter = painterResource(id = R.drawable.ic_chart),
											contentDescription = null
										)
									}
									
									if (!wallet.defaultWallet) {
										IconButton(
											onClick = {
												isDeleteConfirmationPopupShowing = true
											},
											modifier = Modifier
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
										showSelectWalletSheetState()
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
											text = stringResource(id = R.string.balance),
											style = MaterialTheme.typography.bodyMedium.copy(
												fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
											),
											modifier = Modifier
												.weight(0.4f)
										)
										
										Text(
											text = CurrencyFormatter.format(
												locale = deviceLocale,
												amount = wallet.balance,
												useSymbol = true,
												currencyCode = LocalCurrency.current.countryCode
											),
											textAlign = TextAlign.End,
											style = MaterialTheme.typography.bodyMedium.copy(
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
												color = incomeColor.darkenColor(0.14f),
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
												color = expenseColor.darkenColor(0.14f),
												fontWeight = FontWeight.Medium,
												fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
											),
											modifier = Modifier
												.weight(0.6f)
										)
									}
								}
							}
							
							Text(
								text = stringResource(id = R.string.transaction),
								style = MaterialTheme.typography.bodyLarge.copy(
									fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
								),
								modifier = Modifier
									.padding(16.dpScaled)
							)
						}
					}
					
					items(
						items = mixedFinancialList,
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
								
								showFinancialSheetState()
							},
							modifier = Modifier
								.padding(horizontal = 12.dpScaled)
								.testTag("SwipeableFinancialCard")
						)
					}
				}
			}
		}
	}
}
