package com.anafthdev.dujer.ui.budget_list.subscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.common.AppUtil.toast
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.common.TextFieldCurrencyFormatter
import com.anafthdev.dujer.foundation.extension.*
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.budget_list.component.SelectBudgetCategoryCard
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.small_shape
import com.anafthdev.dujer.ui.theme.warning_color
import com.anafthdev.dujer.uicomponent.TopAppBar
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddBudgetScreen(
	isScreenVisible: Boolean,
	showTopSnackbar: Boolean,
	averagePerMonthCategory: List<Pair<Double, Category>>,
	onBack: () ->Unit,
	onAdded: (Budget) -> Unit,
	onBudgetExists: (Boolean) -> Unit
) {
	
	val uiMode = LocalUiMode.current
	val context = LocalContext.current
	val dujerState = LocalDujerState.current
	val localCurrency = LocalCurrency.current
	val keyboardController = LocalSoftwareKeyboardController.current
	
	val allBudget = dujerState.allBudget
	
	val categories = remember(dujerState.allCategory) {
		dujerState.allCategory
			.filter { it.type == FinancialType.EXPENSE }
			.sortedBy { it.name }
	}
	
	var budgetAmountDouble: Double by rememberSaveable { mutableStateOf(0.0) }
	var budgetAmount: TextFieldValue by remember { mutableStateOf(TextFieldValue()) }
	
	var selectedCategoryID by rememberSaveable { mutableStateOf(Category.otherExpense.id) }
	val selectedCategory = remember(selectedCategoryID) {
		categories.find { it.id == selectedCategoryID } ?: Category.default
	}
	
	LaunchedEffect(isScreenVisible) {
		budgetAmountDouble = 0.0
		budgetAmount = TextFieldValue(
			text = CurrencyFormatter.format(
				locale = deviceLocale,
				amount = 0.0,
				useSymbol = false,
				currencyCode = localCurrency.countryCode
			)
		)
		selectedCategoryID = Category.otherExpense.id
		
		keyboardController?.hide()
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
					contentDescription = null
				)
			}
			
			IconButton(
				onClick = {
					if (budgetAmountDouble != 0.0 || budgetAmount.text.isNotBlank()) {
						val isBudgetExists = allBudget.containBy { it.category.id == selectedCategoryID }
						
						if (!isBudgetExists) {
							onAdded(
								Budget(
									id = Random(System.currentTimeMillis()).nextInt(),
									max = budgetAmountDouble,
									category = selectedCategory,
								)
							)
						} else onBudgetExists(true)
					} else context.getString(R.string.amount_cannot_be_empty).toast(context)
				},
				modifier = Modifier
					.padding(end = 8.dpScaled)
					.align(Alignment.CenterEnd)
			) {
				Icon(
					imageVector = Icons.Rounded.Check,
					contentDescription = null
				)
			}
		}
		
		AnimatedVisibility(
			visible = showTopSnackbar,
			modifier = Modifier
				.padding(
					vertical = 8.dpScaled,
					horizontal = 16.dpScaled
				)
		) {
			Card(
				colors = CardDefaults.cardColors(
					containerColor = Color.Transparent
				),
				border = BorderStroke(
					width = 1.dpScaled,
					color = selectedCategory.tint.iconTint.toColor()
				)
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.padding(16.dpScaled)
						.fillMaxWidth()
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_danger_triangle),
						contentDescription = null,
						tint = warning_color.darkenColor(0.05f),
						modifier = Modifier
							.size(32.dpScaled)
					)
					
					Text(
						maxLines = 2,
						overflow = TextOverflow.Ellipsis,
						text = buildAnnotatedString {
							val s = stringResource(
								id = R.string.budget_with_category_x_already_exists,
								selectedCategory.name
							)
							
							val (startIndex, endIndex) = s.indexOf(selectedCategory.name)
							
							withStyle(
								MaterialTheme.typography.bodyMedium.copy(
									color = LocalUiColor.current.bodyText
								).toSpanStyle()
							) {
								append(s)
							}
							
							addStyle(
								style = MaterialTheme.typography.bodyMedium.copy(
									color = LocalUiColor.current.titleText,
									fontWeight = FontWeight.SemiBold
								).toSpanStyle(),
								start = startIndex,
								end = endIndex + 1
							)
						},
						style = MaterialTheme.typography.bodyMedium.copy(
							fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
						),
						modifier = Modifier
							.padding(start = 8.dpScaled)
					)
				}
			}
		}
		
		Text(
			text = stringResource(id = R.string.set_a_monthly_budget),
			style = MaterialTheme.typography.bodyLarge.copy(
				color = LocalUiColor.current.bodyText,
				fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
			),
			modifier = Modifier
				.padding(
					vertical = 8.dpScaled,
					horizontal = 16.dpScaled
				)
		)
		
		OutlinedTextField(
			value = budgetAmount,
			singleLine = true,
			shape = small_shape,
			textStyle = LocalTextStyle.current.copy(
				fontFamily = Inter
			),
			onValueChange = { s ->
				val formattedValue = TextFieldCurrencyFormatter.getFormattedCurrency(
					fieldValue = s,
					countryCode = localCurrency.countryCode
				)
				
				budgetAmountDouble = formattedValue.first
				budgetAmount = formattedValue.second
			},
			leadingIcon = {
				Text(
					text = CurrencyFormatter.getSymbol(
						locale = deviceLocale,
						currencyCode = LocalCurrency.current.countryCode
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
				.padding(
					start = 16.dpScaled,
					end = 16.dpScaled,
					bottom = 16.dpScaled,
				)
				.fillMaxWidth()
		)
		
		Text(
			text = stringResource(id = R.string.choose_your_budget_category),
			style = MaterialTheme.typography.bodyLarge.copy(
				color = LocalUiColor.current.bodyText,
				fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
			),
			modifier = Modifier
				.padding(
					vertical = 8.dpScaled,
					horizontal = 16.dpScaled
				)
		)
		
		categories.forEach { category ->
			
			val averagePerMonth = remember(averagePerMonthCategory) {
				averagePerMonthCategory.find { it.second.id == category.id }?.first ?: 0.0
			}
			
			SelectBudgetCategoryCard(
				selected = category.id == selectedCategoryID,
				category = category,
				averagePerMonth = averagePerMonth,
				onClick = {
					onBudgetExists(false)
					selectedCategoryID = category.id
				},
				modifier = Modifier
					.padding(
						vertical = 8.dpScaled,
						horizontal = 16.dpScaled
					)
			)
		}
		
		Spacer(modifier = Modifier.height(16.dpScaled))
	}
	
}
