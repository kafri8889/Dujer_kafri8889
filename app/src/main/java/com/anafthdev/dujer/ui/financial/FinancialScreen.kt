package com.anafthdev.dujer.ui.financial

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.get
import com.anafthdev.dujer.foundation.extension.showDatePicker
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.financial.component.CategoryList
import com.anafthdev.dujer.ui.financial.component.WalletList
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.theme.*
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.util.AppUtil.toast
import com.anafthdev.dujer.util.CurrencyFormatter
import com.anafthdev.dujer.util.TextFieldCurrencyFormatter
import timber.log.Timber
import java.text.SimpleDateFormat
import kotlin.random.Random

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialScreen(
	isScreenVisible: Boolean,
	financial: Financial,
	financialAction: String,
	onBack: () -> Unit,
	onSave: () -> Unit
) {
	
	val context = LocalContext.current
	val focusManager = LocalFocusManager.current
	val localCurrency = LocalCurrency.current
	
	val financialViewModel = hiltViewModel<FinancialViewModel>()
	
	val state by financialViewModel.state.collectAsState()
	
	val wallets = state.wallets
	val categories = state.categories
	
	var financialNew by remember { mutableStateOf(Financial.default.copy(id = -1)) }
	var financialTitle: String by remember { mutableStateOf("") }
	var financialAmountDouble: Double by remember { mutableStateOf(0.0) }
	var financialAmount: TextFieldValue by remember { mutableStateOf(TextFieldValue()) }
	var financialDate: Long by remember { mutableStateOf(System.currentTimeMillis()) }
	var financialCategory: Category by remember { mutableStateOf(Category.default) }
	var financialType: FinancialType by remember { mutableStateOf(FinancialType.INCOME) }
	
	val textFieldDateFocusRequester = remember { FocusRequester() }
	val textFieldCategoryFocusRequester = remember { FocusRequester() }
	val textFieldWalletFocusRequester = remember { FocusRequester() }
	var categoryFocusRequesterHasFocus by remember { mutableStateOf(false) }
	var walletFocusRequesterHasFocus by remember { mutableStateOf(false) }
	var dateFocusRequesterHasFocus by remember { mutableStateOf(false) }
	var isCategoryListShowed by remember { mutableStateOf(false) }
	var isWalletListShowed by remember { mutableStateOf(false) }
	var selectedWallet by remember { mutableStateOf(Wallet.cash) }
	
	val resetFinancial = {
		selectedWallet = Wallet.cash
		financialNew = Financial.default
		financialTitle = ""
		financialDate = System.currentTimeMillis()
		financialCategory = Category.default
		financialType = FinancialType.INCOME
		financialAmountDouble = 0.0
		financialAmount = TextFieldValue(
			text = CurrencyFormatter.format(
				locale = deviceLocale,
				amount = 0.0,
				useSymbol = false,
				currencyCode = financial.currency.countryCode
			)
		)
	}
	
	val saveFinancial = {
		onSave()
		resetFinancial()
	}
	
	when {
		(financialAction == FinancialAction.EDIT) and (financial.id != financialNew.id) -> {
			financialNew = financial
			financialTitle = financial.name
			financialDate = financial.dateCreated
			financialCategory = financial.category
			financialType = financial.type
			financialAmountDouble = financial.amount
			financialAmount = financialAmount.copy(
				CurrencyFormatter.format(
					locale = deviceLocale,
					amount = financial.amount,
					useSymbol = false,
					currencyCode = financial.currency.countryCode
				)
			)
			
			selectedWallet = wallets.get { it.id == financial.walletID } ?: Wallet.cash
		}
	}
	
	LaunchedEffect(isScreenVisible) {
		resetFinancial()
		focusManager.clearFocus(force = true)
	}
	
	DisposableEffect(dateFocusRequesterHasFocus) {
		onDispose {
			if (dateFocusRequesterHasFocus) {
				(context as AppCompatActivity).showDatePicker(
					selection = financialDate,
					onPick = { timeInMillis ->
						financialDate = timeInMillis
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
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.systemBarsPadding()
			.verticalScroll(rememberScrollState())
	) {
		TopAppBar {
			IconButton(
				onClick = onBack,
				modifier = Modifier
					.padding(start = 8.dpScaled)
					.align(Alignment.CenterStart)
			) {
				Icon(
					imageVector = Icons.Rounded.ArrowBack,
					tint = black04,
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
				style = Typography.headlineSmall.copy(
					fontWeight = FontWeight.SemiBold,
					fontSize = Typography.headlineSmall.fontSize.spScaled
				)
			)
			
			Column(
				modifier = Modifier
					.padding(horizontal = 8.dpScaled)
					.fillMaxWidth()
			) {
				Text(
					text = stringResource(id = R.string.title),
					style = Typography.bodyLarge.copy(
						fontWeight = FontWeight.Medium,
						fontSize = Typography.bodyLarge.fontSize.spScaled
					),
					modifier = Modifier
						.padding(top = 24.dpScaled)
				)
				
				OutlinedTextField(
					value = financialTitle,
					singleLine = true,
					shape = small_shape,
					textStyle = LocalTextStyle.current.copy(
						fontFamily = Inter
					),
					onValueChange = { s ->
						financialTitle = s
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
					style = Typography.bodyLarge.copy(
						fontWeight = FontWeight.Medium,
						fontSize = Typography.bodyLarge.fontSize.spScaled
					),
					modifier = Modifier
						.padding(top = 24.dpScaled)
				)
				
				OutlinedTextField(
					value = financialAmount,
					singleLine = true,
					shape = small_shape,
					textStyle = LocalTextStyle.current.copy(
						fontFamily = Inter
					),
					onValueChange = { s ->
						val formattedValue = TextFieldCurrencyFormatter.getFormattedCurrency(
							fieldValue = s,
							countryCode = financial.currency.countryCode
						)
						
						financialAmountDouble = formattedValue.first
						financialAmount = formattedValue.second
					},
					leadingIcon = {
						Text(
							text = if (financialAction == FinancialAction.EDIT) {
								financialNew.currency.symbol
							} else CurrencyFormatter.getSymbol(
								locale = deviceLocale,
								currencyCode = localCurrency.countryCode
							),
							style = Typography.bodyMedium.copy(
								fontWeight = FontWeight.Medium,
								fontSize = Typography.bodyMedium.fontSize.spScaled
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
					style = Typography.bodyLarge.copy(
						fontWeight = FontWeight.Medium,
						fontSize = Typography.bodyLarge.fontSize.spScaled
					),
					modifier = Modifier
						.padding(top = 24.dpScaled)
				)
				
				OutlinedTextField(
					value = SimpleDateFormat("dd MMM yyyy", deviceLocale).format(
						financialDate
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
					style = Typography.bodyLarge.copy(
						fontWeight = FontWeight.Medium,
						fontSize = Typography.bodyLarge.fontSize.spScaled
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
						categories = categories,
						onItemClick = { category ->
							financialCategory = category
							isCategoryListShowed = false
						}
					)
				}
				
				Text(
					text = stringResource(id = R.string.wallet),
					style = Typography.bodyLarge.copy(
						fontWeight = FontWeight.Medium,
						fontSize = Typography.bodyLarge.fontSize.spScaled
					),
					modifier = Modifier
						.padding(top = 24.dpScaled)
				)
				
				OutlinedTextField(
					value = selectedWallet.name,
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
								.clip(MaterialTheme.shapes.small)
								.border(
									width = 1.dpScaled,
									color = MaterialTheme.colorScheme.outline,
									shape = medium_shape
								)
								.background(selectedWallet.tint.backgroundTint.toColor())
								.align(Alignment.CenterHorizontally)
						) {
							Icon(
								painter = painterResource(id = selectedWallet.iconID),
								tint = selectedWallet.tint.iconTint.toColor(),
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
						wallets = wallets,
						onWalletSelected = { wallet ->
							selectedWallet = wallet
							isWalletListShowed = false
						}
					)
				}
				
				Text(
					text = stringResource(id = R.string.type),
					style = Typography.bodyLarge.copy(
						fontWeight = FontWeight.Medium,
						fontSize = Typography.bodyLarge.fontSize.spScaled
					),
					modifier = Modifier
						.padding(top = 24.dpScaled)
				)
				
				Row(
					modifier = Modifier
						.padding(top = 8.dpScaled)
						.fillMaxWidth()
				) {
					FilterChip(
						selected = financialType == FinancialType.INCOME,
						colors = FilterChipDefaults.filterChipColors(
							selectedContainerColor = income_card_background
						),
						label = {
							Row(
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.Center,
								modifier = Modifier
									.fillMaxSize()
							) {
								Text(
									text = stringResource(id = R.string.income),
									style = MaterialTheme.typography.bodyMedium.copy(
										textAlign = TextAlign.Center,
										fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
									)
								)
							}
						},
						onClick = {
							financialType = FinancialType.INCOME
						},
						modifier = Modifier
							.padding(
								start = 4.dpScaled,
								end = 2.dpScaled
							)
							.height(36.dpScaled)
							.weight(1f)
					)
					
					FilterChip(
						selected = financialType == FinancialType.EXPENSE,
						colors = FilterChipDefaults.filterChipColors(
							selectedContainerColor = expense_card_background
						),
						label = {
							Row(
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.Center,
								modifier = Modifier
									.fillMaxSize()
							) {
								Text(
									text = stringResource(id = R.string.expenses),
									style = MaterialTheme.typography.bodyMedium.copy(
										textAlign = TextAlign.Center,
										fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
									)
								)
							}
						},
						onClick = {
							financialType = FinancialType.EXPENSE
						},
						modifier = Modifier
							.padding(
								start = 2.dpScaled,
								end = 4.dpScaled
							)
							.height(36.dpScaled)
							.weight(1f)
					)
				}
				
				FilledTonalButton(
					onClick = {
						Timber.i("save: action $financialAction")
						Timber.i("save (new):  $financialNew")
						Timber.i("save (old):  $financial")
						if (financialAction == FinancialAction.EDIT) {
							financialViewModel.dispatch(
								com.anafthdev.dujer.ui.financial.FinancialAction.Update(
									financialNew.copy(
										name = financialTitle,
										amount = financialAmountDouble,
										dateCreated = financialDate,
										category = financialCategory,
										type = financialType,
										walletID = selectedWallet.id
									)
								)
							)
							
							saveFinancial()
						} else {
							when {
								financialTitle.isBlank() -> context.getString(
									R.string.title_cannot_be_empty
								).toast(context)
								financialAmount.text.isBlank() -> context.getString(
									R.string.amount_cannot_be_empty
								).toast(context)
								financialCategory.id == Category.default.id -> context.getString(
									R.string.category_cannot_be_empty
								).toast(context)
								else -> {
									financialViewModel.dispatch(
										com.anafthdev.dujer.ui.financial.FinancialAction.Insert(
											Financial(
												id = Random.nextInt(),
												name = financialTitle,
												amount = financialAmountDouble,
												type = financialType,
												category = financialCategory,
												currency = localCurrency,
												dateCreated = financialDate,
												walletID = selectedWallet.id
											)
										)
									)
									saveFinancial()
								}
							}
						}
					},
					modifier = Modifier
						.padding(vertical = 24.dpScaled)
						.fillMaxWidth()
				) {
					Text(
						text = stringResource(id = R.string.save)
					)
				}
				
			}
		}
	}
}
