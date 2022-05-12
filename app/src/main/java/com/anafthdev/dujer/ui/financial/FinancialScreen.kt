package com.anafthdev.dujer.ui.financial

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.*
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.financial.component.CategoryList
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.theme.*
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.util.AppUtil.toast
import com.anafthdev.dujer.util.CurrencyFormatter
import timber.log.Timber
import java.text.SimpleDateFormat
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialScreen(
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
	
	val categories = state.categories
	
	val textFieldDateFocusRequester = remember { FocusRequester() }
	val textFieldCategoryFocusRequester = remember { FocusRequester() }
	var categoryFocusRequesterHasFocus by remember { mutableStateOf(false) }
	var dateFocusRequesterHasFocus by remember { mutableStateOf(false) }
	
	var financialNew by remember { mutableStateOf(Financial.default.copy(id = -1)) }
	var financialTitle: String by remember { mutableStateOf("") }
	var financialAmountDouble: Double by remember { mutableStateOf(0.0) }
	var financialAmount: TextFieldValue by remember { mutableStateOf(TextFieldValue()) }
	var financialDate: Long by remember { mutableStateOf(System.currentTimeMillis()) }
	var financialCategory: Category by remember { mutableStateOf(Category.default) }
	var financialType: FinancialType by remember { mutableStateOf(FinancialType.INCOME) }
	
	var isCategoryListShowed by remember { mutableStateOf(false) }
	
	val resetFinancial = {
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
		}
	}
	
	Timber.i("${financial.name} ?= ${financialNew.name}")
	
	DisposableEffect(financialAction) {
		onDispose {
			resetFinancial()
			focusManager.clearFocus(force = true)
		}
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
		if (!isCategoryListShowed and categoryFocusRequesterHasFocus) {
			focusManager.clearFocus(force = true)
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
						var selectionIndex = s.selection
						var amount = s.text.replace(
							oldValue = listOf("-", " "),
							newValue = "",
							ignoreCase = true
						)
						
						if (amount != s.text) {
							selectionIndex = TextRange(selectionIndex.start - 1)
						}
						
						while (amount.startsWith(listOf(",", "."))) {
							amount = amount.replaceFirstChar("")
							selectionIndex = TextRange.Zero
						}
						
						financialAmountDouble = CurrencyFormatter.parse(
							locale = deviceLocale,
							amount = "${CurrencyFormatter.getSymbol(deviceLocale, financial.currency.countryCode)}$amount",
							currencyCode = financial.currency.countryCode,
						)
						
						Timber.i("amont format from: ${financialViewModel.deviceCurrency.symbol}$amount")
						Timber.i("amont: $financialAmountDouble")
						Timber.i("amont s: $amount")
						
						financialAmount = s.copy(
							text = CurrencyFormatter.format(
								locale = deviceLocale,
								amount = financialAmountDouble,
								useSymbol = false,
								currencyCode = financial.currency.countryCode
							),
							selection = selectionIndex
						)
						
						Timber.i("financial amont: ${financialAmount.text}")
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
					text = stringResource(id = R.string.type),
					style = Typography.bodyLarge.copy(
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
						if (financialAction == FinancialAction.EDIT) {
							financialViewModel.dispatch(
								com.anafthdev.dujer.ui.financial.FinancialAction.Update(
									financialNew.copy(
										name = financialTitle,
										amount = financialAmountDouble,
										dateCreated = financialDate,
										category = financialCategory,
										type = financialType
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
												dateCreated = financialDate
											)
										)
									)
									Timber.i("invoke new financial")
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
