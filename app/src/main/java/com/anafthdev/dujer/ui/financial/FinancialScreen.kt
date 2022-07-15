package com.anafthdev.dujer.ui.financial

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.common.AppUtil.toast
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.common.TextFieldCurrencyFormatter
import com.anafthdev.dujer.foundation.common.showDatePicker
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.isIncome
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.runtime.MainActivity
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.financial.FinancialAction.*
import com.anafthdev.dujer.ui.financial.component.CategoryList
import com.anafthdev.dujer.ui.financial.component.MaxBudgetReachedPopup
import com.anafthdev.dujer.ui.financial.component.WalletList
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.small_shape
import com.anafthdev.dujer.uicomponent.FinancialTypeSelector
import com.anafthdev.dujer.uicomponent.TopAppBar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@SuppressLint("RememberReturnType")
@Composable
fun FinancialScreen(
	navController: NavController,
	isScreenVisible: Boolean,
	financialID: Int,
	financialAction: String
) {
	
	val context = LocalContext.current
	val dujerState = LocalDujerState.current
	val localCurrency = LocalCurrency.current
	val focusManager = LocalFocusManager.current
	
	val viewModel = hiltViewModel<FinancialViewModel>()
	
	val state by viewModel.state.collectAsState()
	
	val allWallet = dujerState.allWallet
	val allCategory = dujerState.allCategory
	val allBudget = dujerState.allBudget
	
	var currentFinancialMaxBudgetPopup by remember { mutableStateOf(Financial.default) }
	var currentBudgetMaxBudgetPopup by remember { mutableStateOf(Budget.defalut) }
	
	val financialCategory = remember(
		state.financialType,
		state.financialCategoryForIncome,
		state.financialCategoryForExpense
	) {
		if (state.financialType.isIncome()) state.financialCategoryForIncome else state.financialCategoryForExpense
	}
	
	val calendar = remember { Calendar.getInstance() }
	val textFieldDateFocusRequester = remember { FocusRequester() }
	val textFieldCategoryFocusRequester = remember { FocusRequester() }
	val textFieldWalletFocusRequester = remember { FocusRequester() }
	var categoryFocusRequesterHasFocus by remember { mutableStateOf(false) }
	var isMaxBudgetReachedPopupShowed by remember { mutableStateOf(false) }
	var walletFocusRequesterHasFocus by remember { mutableStateOf(false) }
	var dateFocusRequesterHasFocus by remember { mutableStateOf(false) }
	var isCategoryListShowed by remember { mutableStateOf(false) }
	var isWalletListShowed by remember { mutableStateOf(false) }
	
	val categorySelectorItems = remember(allCategory, state.financialType) {
		allCategory.filter { it.type == state.financialType }
	}
	
	val saveFinancial = {
		viewModel.dispatch(
			ResetState(
				currencyCode = state.selectedFinancial.currency.countryCode
			)
		)
		
		navController.popBackStack()
	}
	
	when {
		(financialAction == FinancialAction.EDIT) and (financialID != state.financialNew.id) -> {
			viewModel.dispatch(
				SetFinancialNew(
					financial = state.selectedFinancial
				)
			)
			
			viewModel.dispatch(
				SetFinancialTitle(
					title = state.selectedFinancial.name
				)
			)
			
			viewModel.dispatch(
				SetFinancialDate(
					date = state.selectedFinancial.dateCreated
				)
			)
			
			viewModel.dispatch(
				SetFinancialType(
					type = state.selectedFinancial.type
				)
			)
			
			viewModel.dispatch(
				SetFinancialAmountDouble(
					amount = state.selectedFinancial.amount
				)
			)
			
			viewModel.dispatch(
				SetFinancialAmount(
					value = state.financialAmount.copy(
						text = CurrencyFormatter.format(
							locale = deviceLocale,
							amount = state.selectedFinancial.amount,
							useSymbol = false,
							currencyCode = state.selectedFinancial.currency.countryCode
						)
					)
				)
			)
			
			viewModel.dispatch(
				if (state.selectedFinancial.type.isIncome()) {
					SetFinancialCategoryForIncome(
						category = state.selectedFinancial.category
					)
				} else {
					SetFinancialCategoryForExpense(
						category = state.selectedFinancial.category
					)
				}
			)
			
			viewModel.dispatch(
				SetSelectedWallet(
					wallet = allWallet.find { it.id == state.selectedFinancial.walletID } ?: Wallet.cash
				)
			)
		}
	}
	
	LaunchedEffect(financialID) {
		viewModel.dispatch(
			GetFinancial(
				id = financialID
			)
		)
	}
	
	LaunchedEffect(isScreenVisible) {
		if (!isScreenVisible) {
			viewModel.dispatch(
				ResetState(
					currencyCode = state.selectedFinancial.currency.countryCode
				)
			)
		}
		
		focusManager.clearFocus(force = true)
	}
	
	DisposableEffect(dateFocusRequesterHasFocus) {
		onDispose {
			if (dateFocusRequesterHasFocus) {
				(context as MainActivity).showDatePicker(
					selection = state.financialDate,
					min = calendar.apply {
						set(Calendar.YEAR, 2000)
					}.timeInMillis,
					onPick = { timeInMillis ->
						viewModel.dispatch(
							SetFinancialDate(
								date = timeInMillis
							)
						)
						
						focusManager.moveFocus(FocusDirection.Next)
					},
					onCancel = {
						focusManager.moveFocus(FocusDirection.Next)
					}
				)
			}
		}
	}
	
	SideEffect {
		when {
			!isCategoryListShowed and categoryFocusRequesterHasFocus -> focusManager.clearFocus(force = true)
			!isWalletListShowed and walletFocusRequesterHasFocus -> focusManager.clearFocus(force = true)
		}
	}
	
	BackHandler {
		navController.popBackStack()
	}
	
	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		AnimatedVisibility(
			visible = isMaxBudgetReachedPopupShowed,
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
			MaxBudgetReachedPopup(
				budget = currentBudgetMaxBudgetPopup,
				financial = currentFinancialMaxBudgetPopup,
				financialEdit = state.financialNew,
				onClose = {
					isMaxBudgetReachedPopupShowed = false
				}, onIgnore = {
					isMaxBudgetReachedPopupShowed = false
					viewModel.dispatch(
						if (financialAction == FinancialAction.EDIT) {
							Update(currentFinancialMaxBudgetPopup)
						} else Insert(currentFinancialMaxBudgetPopup)
					)
					
					saveFinancial()
				}
			)
		}
		
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
				.systemBarsPadding()
				.verticalScroll(rememberScrollState())
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
			}
			
			Column(
				modifier = Modifier
					.padding(24.dpScaled)
			) {
				Text(
					text = stringResource(
						id = if (financialAction == FinancialAction.NEW) R.string._new
						else R.string.edit
					),
					style = MaterialTheme.typography.headlineSmall.copy(
						color = LocalUiColor.current.headlineText,
						fontWeight = FontWeight.SemiBold,
						fontSize = MaterialTheme.typography.headlineSmall.fontSize.spScaled
					)
				)
				
				Column(
					modifier = Modifier
						.padding(horizontal = 8.dpScaled)
						.fillMaxWidth()
				) {
					Text(
						text = stringResource(id = R.string.title),
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.Medium,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.padding(top = 24.dpScaled)
					)
					
					OutlinedTextField(
						value = state.financialTitle,
						singleLine = true,
						shape = small_shape,
						textStyle = LocalTextStyle.current.copy(
							fontFamily = Inter
						),
						onValueChange = { s ->
							viewModel.dispatch(
								SetFinancialTitle(
									title = s
								)
							)
						},
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Text,
							imeAction = ImeAction.Next
						),
						modifier = Modifier
							.padding(top = 8.dpScaled)
							.fillMaxWidth()
					)
					
					Text(
						text = stringResource(id = R.string.amount),
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.Medium,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.padding(top = 24.dpScaled)
					)
					
					OutlinedTextField(
						value = state.financialAmount,
						singleLine = true,
						shape = small_shape,
						textStyle = LocalTextStyle.current.copy(
							fontFamily = Inter
						),
						onValueChange = { s ->
							val formattedValue = TextFieldCurrencyFormatter.getFormattedCurrency(
								fieldValue = s,
								countryCode = state.selectedFinancial.currency.countryCode
							)
							
							viewModel.dispatch(
								SetFinancialAmountDouble(
									amount = formattedValue.first
								)
							)
							
							viewModel.dispatch(
								SetFinancialAmount(
									value = formattedValue.second
								)
							)
						},
						leadingIcon = {
							Text(
								text = if (financialAction == FinancialAction.EDIT) {
									state.financialNew.currency.symbol
								} else CurrencyFormatter.getSymbol(
									locale = deviceLocale,
									currencyCode = localCurrency.countryCode
								),
								style = MaterialTheme.typography.bodyMedium.copy(
									color = LocalUiColor.current.titleText,
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								)
							)
						},
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Number,
							imeAction = ImeAction.Next
						),
						modifier = Modifier
							.padding(top = 8.dpScaled)
							.fillMaxWidth()
					)
					
					Text(
						text = stringResource(id = R.string.date),
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.Medium,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.padding(top = 24.dpScaled)
					)
					
					OutlinedTextField(
						value = SimpleDateFormat("dd MMM yyyy", deviceLocale).format(
							state.financialDate
						),
						singleLine = true,
						readOnly = true,
						shape = small_shape,
						textStyle = LocalTextStyle.current.copy(
							fontFamily = Inter
						),
						onValueChange = {},
						trailingIcon = {
							IconButton(
								onClick = {
									textFieldDateFocusRequester.requestFocus()
								}
							) {
								Icon(
									painter = painterResource(id = R.drawable.ic_calendar),
									contentDescription = null,
								)
							}
						},
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Text,
							imeAction = ImeAction.Next
						),
						modifier = Modifier
							.padding(top = 8.dpScaled)
							.fillMaxWidth()
							.focusRequester(textFieldDateFocusRequester)
							.onFocusChanged { focusState ->
								dateFocusRequesterHasFocus = focusState.hasFocus
							}
					)
					
					Text(
						text = stringResource(id = R.string.category),
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.Medium,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.padding(top = 24.dpScaled)
					)
					
					OutlinedTextField(
						value = financialCategory.name,
						singleLine = true,
						readOnly = true,
						shape = small_shape,
						textStyle = LocalTextStyle.current.copy(
							fontFamily = Inter
						),
						onValueChange = {},
						trailingIcon = {
							IconButton(
								onClick = {
									isCategoryListShowed = !isCategoryListShowed
									textFieldCategoryFocusRequester.requestFocus()
								}
							) {
								Icon(
									imageVector = Icons.Rounded.ArrowDropDown,
									contentDescription = null,
									modifier = Modifier
										.rotate(
											if (isCategoryListShowed) 180f else 0f
										)
								)
							}
						},
						modifier = Modifier
							.padding(top = 8.dpScaled)
							.fillMaxWidth()
							.focusRequester(textFieldCategoryFocusRequester)
							.onFocusChanged { focusState ->
								categoryFocusRequesterHasFocus = focusState.hasFocus.also {
									isCategoryListShowed = it
								}
							}
					)
					
					AnimatedVisibility(
						visible = isCategoryListShowed,
						enter = expandVertically(
							animationSpec = tween(600),
						),
						exit = shrinkVertically(
							animationSpec = tween(600),
						),
						modifier = Modifier
							.padding(top = 8.dpScaled)
					) {
						CategoryList(
							categories = categorySelectorItems,
							onItemClick = { category ->
								viewModel.dispatch(
									if (state.financialType.isIncome()) {
										SetFinancialCategoryForIncome(
											category = category
										)
									} else {
										SetFinancialCategoryForExpense(
											category = category
										)
									}
								)
								
								isCategoryListShowed = false
							}
						)
					}
					
					Text(
						text = stringResource(id = R.string.wallet),
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.Medium,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.padding(top = 24.dpScaled)
					)
					
					OutlinedTextField(
						value = state.selectedWallet.name,
						singleLine = true,
						readOnly = true,
						shape = small_shape,
						textStyle = LocalTextStyle.current.copy(
							fontFamily = Inter
						),
						onValueChange = {},
						leadingIcon = {
							Box(
								modifier = Modifier
									.padding(
										start = 8.dp
									)
									.size(44.dp)
									.clip(MaterialTheme.shapes.medium)
									.border(
										width = 1.dpScaled,
										color = MaterialTheme.colorScheme.outline,
										shape = MaterialTheme.shapes.medium
									)
									.background(state.selectedWallet.tint.backgroundTint.toColor())
									.align(Alignment.CenterHorizontally)
							) {
								Icon(
									painter = painterResource(id = state.selectedWallet.iconID),
									tint = state.selectedWallet.tint.iconTint.toColor(),
									contentDescription = null,
									modifier = Modifier
										.align(Alignment.Center)
								)
							}
						},
						trailingIcon = {
							IconButton(
								onClick = {
									isWalletListShowed = !isWalletListShowed
									textFieldWalletFocusRequester.requestFocus()
								}
							) {
								Icon(
									imageVector = Icons.Rounded.ArrowDropDown,
									contentDescription = null,
									modifier = Modifier
										.rotate(
											if (isWalletListShowed) 180f else 0f
										)
								)
							}
						},
						modifier = Modifier
							.padding(top = 8.dpScaled)
							.fillMaxWidth()
							.focusRequester(textFieldWalletFocusRequester)
							.onFocusChanged { focusState ->
								walletFocusRequesterHasFocus = focusState.hasFocus.also {
									isWalletListShowed = it
								}
							}
					)
					
					AnimatedVisibility(
						visible = isWalletListShowed,
						enter = expandVertically(
							animationSpec = tween(600),
						),
						exit = shrinkVertically(
							animationSpec = tween(600),
						),
						modifier = Modifier
							.padding(top = 8.dpScaled)
					) {
						WalletList(
							wallets = allWallet,
							onWalletSelected = { wallet ->
								viewModel.dispatch(
									SetSelectedWallet(
										wallet = wallet
									)
								)
								
								isWalletListShowed = false
							}
						)
					}
					
					Text(
						text = stringResource(id = R.string.type),
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.Medium,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.padding(top = 24.dpScaled)
					)
					
					FinancialTypeSelector(
						enableDoubleClick = false,
						selectedFinancialType = state.financialType,
						onFinancialTypeChanged = { type ->
							viewModel.dispatch(
								SetFinancialType(
									type = type
								)
							)
						},
						modifier = Modifier
							.padding(top = 8.dpScaled)
					)
					
					Button(
						onClick = {
							when {
								state.financialTitle.isBlank() -> {
									context.getString(R.string.title_cannot_be_empty).toast(context)
									return@Button
								}
								state.financialAmount.text.isBlank() -> {
									context.getString(R.string.amount_cannot_be_empty).toast(context)
									return@Button
								}
								financialCategory.id == Category.default.id -> {
									context.getString(R.string.category_cannot_be_empty).toast(context)
									return@Button
								}
							}
							
							val budget = allBudget.find { it.category.id == financialCategory.id }
							val isMaxReached = budget?.isMaxReached ?: false
							val mFinancial = if (financialAction == FinancialAction.EDIT) {
								state.financialNew.copy(
									name = state.financialTitle,
									amount = state.financialAmountDouble,
									dateCreated = state.financialDate,
									category = financialCategory,
									type = state.financialType,
									walletID = state.selectedWallet.id
								)
							} else {
								val random = Random(System.currentTimeMillis())
								val id = random.nextInt()
								
								Financial(
									id = id,
									name = state.financialTitle,
									amount = state.financialAmountDouble,
									type = state.financialType,
									category = financialCategory,
									currency = localCurrency,
									dateCreated = state.financialDate,
									walletID = state.selectedWallet.id
								)
							}
							
							val updateFinancial = {
								viewModel.dispatch(
									if (financialAction == FinancialAction.EDIT) {
										Update(mFinancial)
									} else Insert(mFinancial)
								)
								
								saveFinancial()
							}
							
							if (isMaxReached) {
								budget?.let {
									currentFinancialMaxBudgetPopup = mFinancial
									currentBudgetMaxBudgetPopup = budget
									isMaxBudgetReachedPopupShowed = true
								}
							} else updateFinancial()
						},
						modifier = Modifier
							.padding(vertical = 24.dpScaled)
							.fillMaxWidth()
					) {
						Text(text = stringResource(id = R.string.save))
					}
					
				}
			}
		}
	}
}
