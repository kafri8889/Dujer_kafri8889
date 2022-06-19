package com.anafthdev.dujer.ui.wallet.subscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.isLightTheme
import com.anafthdev.dujer.foundation.extension.toPositive
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.small_shape
import com.anafthdev.dujer.util.AppUtil.toast
import com.anafthdev.dujer.util.CurrencyFormatter
import com.anafthdev.dujer.util.TextFieldCurrencyFormatter
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditWalletBalanceScreen(
	wallet: Wallet,
	onCancel: () -> Unit,
	onSave: (Wallet, Financial) -> Unit
) {
	
	val uiMode = LocalUiMode.current
	val context = LocalContext.current
	val localCurrency = LocalCurrency.current
	val focusManager = LocalFocusManager.current
	
	var initialBalance by remember { mutableStateOf(0.0) }
	var balanceFieldValue by remember { mutableStateOf(TextFieldValue()) }
	var editBalanceOption by remember { mutableStateOf(EditBalanceOption.CHANGE_BALANCE) }
	
	val balanceFocusRequester = remember { FocusRequester() }
	
	LaunchedEffect(wallet.id) {
		initialBalance = wallet.initialBalance
	}
	
	LaunchedEffect(wallet.initialBalance) {
		initialBalance = wallet.initialBalance
		balanceFieldValue = balanceFieldValue.copy(
			text = CurrencyFormatter.format(
				locale = deviceLocale,
				amount = wallet.initialBalance,
				useSymbol = false,
				currencyCode = localCurrency.countryCode
			)
		)
	}
	
	Column(
		modifier = Modifier
			.imePadding()
			.fillMaxWidth()
			.background(
				if (uiMode.isLightTheme()) MaterialTheme.colorScheme.background
				else MaterialTheme.colorScheme.surfaceVariant
			)
			.padding(
				vertical = 16.dpScaled
			)
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
		) {
			IconButton(
				onClick = onCancel,
				modifier = Modifier
					.align(Alignment.CenterStart)
			) {
				Icon(
					imageVector = Icons.Rounded.Close,
					contentDescription = null
				)
			}
			
			Text(
				text = stringResource(id = R.string.edit_balance),
				style = Typography.titleMedium.copy(
					color = LocalUiColor.current.titleText,
					fontWeight = FontWeight.Bold,
					fontSize = Typography.titleMedium.fontSize.spScaled
				),
				modifier = Modifier
					.align(Alignment.Center)
			)
			
			IconButton(
				onClick = {
					when {
						balanceFieldValue.text.isBlank() -> {
							context.getString(R.string.amount_cannot_be_empty).toast(context)
						}
						else -> {
							val random = Random(System.currentTimeMillis())
							val id = random.nextInt()
							
							// amount - currentBalance
							val amount = initialBalance - wallet.balance
							
							val mWallet = wallet.copy(
								balance = wallet.balance + amount,
								initialBalance = if (editBalanceOption == EditBalanceOption.CHANGE_INITIAL_AMOUNT) initialBalance
								else wallet.initialBalance
							)
							
							val financial = Financial(
								id = id,
								name = "Transaction",
								amount = amount.toPositive(),
								type = if (amount >= 0) FinancialType.INCOME else FinancialType.EXPENSE,
								walletID = mWallet.id,
								category = Category.transaction,
								currency = localCurrency,
								dateCreated = System.currentTimeMillis()
							)
							
							onSave(
								mWallet,
								if (editBalanceOption == EditBalanceOption.CHANGE_BALANCE) financial
								else Financial.default
							)
						}
					}
				},
				modifier = Modifier
					.align(Alignment.CenterEnd)
			) {
				Icon(
					imageVector = Icons.Rounded.Check,
					contentDescription = null
				)
			}
		}
		
		OutlinedTextField(
			singleLine = true,
			shape = small_shape,
			value = balanceFieldValue,
			onValueChange = { s ->
				val formattedValue = TextFieldCurrencyFormatter.getFormattedCurrency(
					fieldValue = s,
					countryCode = localCurrency.countryCode
				)
				
				initialBalance = formattedValue.first
				balanceFieldValue = formattedValue.second
			},
			textStyle = LocalTextStyle.current.copy(
				fontFamily = Inter
			),
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.Number,
				imeAction = ImeAction.Done
			),
			keyboardActions = KeyboardActions(
				onDone = {
					focusManager.clearFocus(force = true)
				}
			),
			placeholder = {
				Text(stringResource(id = R.string.cash))
			},
			leadingIcon = {
				Text(
					text = CurrencyFormatter.getSymbol(
						locale = deviceLocale,
						currencyCode = localCurrency.countryCode
					),
					style = Typography.bodyMedium.copy(
						fontWeight = FontWeight.Medium,
						fontSize = Typography.bodyMedium.fontSize.spScaled
					)
				)
			},
			modifier = Modifier
				.padding(
					vertical = 8.dpScaled,
					horizontal = 16.dpScaled
				)
				.fillMaxWidth()
				.focusRequester(balanceFocusRequester)
		)
		
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					vertical = 4.dpScaled
				)
				.clickable {
					editBalanceOption = EditBalanceOption.CHANGE_BALANCE
				}
		) {
			RadioButton(
				selected = editBalanceOption == EditBalanceOption.CHANGE_BALANCE,
				onClick = {
					editBalanceOption = EditBalanceOption.CHANGE_BALANCE
				},
				modifier = Modifier
					.padding(
						horizontal = 8.dpScaled
					)
			)
			
			Column {
				Text(
					text = stringResource(id = R.string.change_balance),
					style = Typography.bodyMedium.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.Normal,
						fontSize = Typography.bodyMedium.fontSize.spScaled
					)
				)
				
				AnimatedVisibility(
					visible = editBalanceOption == EditBalanceOption.CHANGE_BALANCE
				) {
					Text(
						text = stringResource(id = R.string.new_transaction_will_be_added),
						style = Typography.bodySmall.copy(
							color = LocalUiColor.current.bodyText,
							fontWeight = FontWeight.Normal,
							fontSize = Typography.bodySmall.fontSize.spScaled
						)
					)
				}
			}
		}
		
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					vertical = 4.dpScaled
				)
				.clickable {
					editBalanceOption = EditBalanceOption.CHANGE_INITIAL_AMOUNT
				}
		) {
			RadioButton(
				selected = editBalanceOption == EditBalanceOption.CHANGE_INITIAL_AMOUNT,
				onClick = {
					editBalanceOption = EditBalanceOption.CHANGE_INITIAL_AMOUNT
				},
				modifier = Modifier
					.padding(
						horizontal = 8.dpScaled
					)
			)
			
			Text(
				text = stringResource(id = R.string.change_initial_amount),
				style = Typography.bodyMedium.copy(
					color = LocalUiColor.current.titleText,
					fontWeight = FontWeight.Normal,
					fontSize = Typography.bodyMedium.fontSize.spScaled
				)
			)
		}
	}
}

private enum class EditBalanceOption {
	CHANGE_BALANCE,
	CHANGE_INITIAL_AMOUNT
}
