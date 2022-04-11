package com.anafthdev.dujer.ui.screen.financial

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.replace
import com.anafthdev.dujer.foundation.extension.replaceFirstChar
import com.anafthdev.dujer.foundation.extension.showDatePicker
import com.anafthdev.dujer.foundation.extension.startsWith
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.app.DujerViewModel
import com.anafthdev.dujer.ui.component.TopAppBar
import com.anafthdev.dujer.ui.screen.financial.component.CategoryList
import com.anafthdev.dujer.ui.screen.financial.component.FinancialTypeList
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.black04
import com.anafthdev.dujer.ui.theme.small_shape
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.AppUtil.toast
import com.anafthdev.dujer.util.CurrencyFormatter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@Composable
fun FinancialScreen(
	financialID: Int,
	financialAction: String
) {
	
	val context = LocalContext.current
	val focusManager = LocalFocusManager.current
	
	val financialViewModel = hiltViewModel<FinancialViewModel>()
	val dujerViewModel = hiltViewModel<DujerViewModel>()
	
	val currentCurrency by financialViewModel.currentCurrency.collectAsState(initial = Currency.INDONESIAN)
	val categories by financialViewModel.categories.observeAsState(initial = Category.values)
	val isFinancialSheetShowed by dujerViewModel.isFinancialSheetShowed.observeAsState(initial = false)
	
	var financial by remember { mutableStateOf(Financial.default) }
	var financialTitle: String by remember { mutableStateOf("") }
	var financialAmountDouble: Double by remember { mutableStateOf(0.0) }
	var financialAmount: TextFieldValue by remember { mutableStateOf(TextFieldValue()) }
	var financialDate: Long by remember { mutableStateOf(System.currentTimeMillis()) }
	var financialCategory: Category by remember { mutableStateOf(Category.default) }
	var financialType: FinancialType by remember { mutableStateOf(FinancialType.INCOME) }
	
	var showCategoryList by remember { mutableStateOf(false) }
	var showFinancialTypeList by remember { mutableStateOf(false) }
	var hasNavigate by remember { mutableStateOf(false) }
	
	when {
		(financialAction == FinancialViewModel.FINANCIAL_ACTION_EDIT) and isFinancialSheetShowed and !hasNavigate -> {
			financialViewModel.getFinancial(financialID) { mFinancial ->
				financial = mFinancial
				financialTitle = financial.name
				financialDate = financial.dateCreated
				financialCategory = financial.category
				financialAmountDouble = financial.amount
				financialAmount = financialAmount.copy(
					CurrencyFormatter.format(
						locale = AppUtil.deviceLocale,
						amount = financial.amount,
						useSymbol = false
					)
				)
				
				true.also {
					hasNavigate = it
				}
			}
		}
	}
	
	if (!isFinancialSheetShowed) {
		financial = Financial.default
		financialTitle = ""
		financialDate = System.currentTimeMillis()
		financialCategory = Category.default
		financialType = FinancialType.INCOME
		financialAmountDouble = 0.0
		financialAmount = TextFieldValue(
			text = CurrencyFormatter.format(
				locale = AppUtil.deviceLocale,
				amount = 0.0,
				useSymbol = false
			)
		)
		
		focusManager.clearFocus(force = true)
		hasNavigate = false
	}
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.verticalScroll(rememberScrollState())
	) {
		TopAppBar {
			IconButton(
				onClick = {
					dujerViewModel.reset()
				},
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
					id = if (financialAction == FinancialViewModel.FINANCIAL_ACTION_NEW) R.string._new
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
							locale = AppUtil.deviceLocale,
							amount = "${currentCurrency.symbol}$amount"
						)
						Timber.i("amont: $financialAmountDouble")
						Timber.i("amont s: $amount")
						financialAmount = s.copy(
							text = CurrencyFormatter.format(
								locale = AppUtil.deviceLocale,
								amount = financialAmountDouble,
								useSymbol = false
							),
							selection = selectionIndex
						)
					},
					leadingIcon = {
						Text(
							text = if (financialAction == FinancialViewModel.FINANCIAL_ACTION_EDIT) {
								financial.currency.symbol
							} else currentCurrency.symbol,
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
					value = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
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
								(context as AppCompatActivity).showDatePicker(
									selection = financialDate
								) { timeInMillis ->
									financialDate = timeInMillis
								}
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
								showCategoryList = !showCategoryList
							}
						) {
							Icon(
								imageVector = Icons.Rounded.ArrowDropDown,
								contentDescription = null,
								modifier = Modifier
									.rotate(
										if (showCategoryList) 180f else 0f
									)
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
				)
				
				AnimatedVisibility(
					visible = showCategoryList,
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
							showCategoryList = false
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
				
				OutlinedTextField(
					value = financialType.name,
					singleLine = true,
					readOnly = true,
					enabled = financialAction == FinancialViewModel.FINANCIAL_ACTION_NEW,
					shape = small_shape,
					textStyle = LocalTextStyle.current.copy(
						fontFamily = Inter
					),
					onValueChange = {},
					trailingIcon = {
						if (financialAction == FinancialViewModel.FINANCIAL_ACTION_NEW) {
							IconButton(
								onClick = {
									showFinancialTypeList = !showFinancialTypeList
								}
							) {
								Icon(
									imageVector = Icons.Rounded.ArrowDropDown,
									contentDescription = null,
									modifier = Modifier
										.rotate(
											if (showFinancialTypeList) 180f else 0f
										)
								)
							}
						}
					},
					keyboardOptions = KeyboardOptions(
						keyboardType = KeyboardType.Text,
						imeAction = ImeAction.Next
					),
					modifier = Modifier
						.padding(top = 8.dpScaled)
						.fillMaxWidth()
				)
				
				AnimatedVisibility(
					visible = showFinancialTypeList,
					enter = expandVertically(
						animationSpec = tween(600),
					),
					exit = shrinkVertically(
						animationSpec = tween(600),
					),
					modifier = Modifier
						.padding(top = 8.dpScaled)
				) {
					FinancialTypeList(
						types = FinancialType.values().toList()
							.filter { it != FinancialType.NOTHING },
						onItemClick = { type ->
							financialType = type
							showFinancialTypeList = false
						}
					)
				}
				
				FilledTonalButton(
					onClick = {
						if (financialAction == FinancialViewModel.FINANCIAL_ACTION_EDIT) {
							financialViewModel.updateFinancial(
								financial = financial.copy(
									name = financialTitle,
									amount = financialAmountDouble,
									dateCreated = financialDate,
									category = financialCategory
								),
								action = {
									dujerViewModel.reset()
								}
							)
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
									financialViewModel.insertFinancial(
										financial = Financial(
											id = Random.nextInt(),
											name = financialTitle,
											amount = financialAmountDouble,
											type = financialType,
											category = financialCategory,
											currency = currentCurrency,
											dateCreated = financialDate
										),
										action = {
											dujerViewModel.reset()
										}
									)
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
