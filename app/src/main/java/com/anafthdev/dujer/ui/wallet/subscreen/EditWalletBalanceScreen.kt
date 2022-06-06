package com.anafthdev.dujer.ui.wallet.subscreen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.isLightTheme
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWalletBalanceScreen(
	wallet: Wallet,
	onCancel: () -> Unit,
	onSave: (Wallet) -> Unit
) {
	// TODO: select option
	val uiMode = LocalUiMode.current
	val context = LocalContext.current
	val localCurrency = LocalCurrency.current
	val focusManager = LocalFocusManager.current
	
	var balance by remember { mutableStateOf(0.0) }
	var balanceFieldValue by remember { mutableStateOf(TextFieldValue()) }
	var editBalanceOption by remember { mutableStateOf(EditBalanceOption.ADD_TRANSACTION) }
	
	val balanceFocusRequester = remember { FocusRequester() }
	
	LaunchedEffect(wallet.id) {
		balance = wallet.balance
	}
	
	LaunchedEffect(wallet.balance) {
		
		balance = wallet.balance
		balanceFieldValue = balanceFieldValue.copy(
			text = CurrencyFormatter.format(
				locale = deviceLocale,
				amount = wallet.balance,
				useSymbol = false,
				currencyCode = localCurrency.countryCode
			)
		)
	}
	
	Column(
		modifier = Modifier
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
				text = stringResource(id = R.string.new_wallet),
				style = Typography.bodyLarge.copy(
					fontWeight = FontWeight.Bold,
					fontSize = Typography.bodyLarge.fontSize.spScaled
				),
				modifier = Modifier
					.align(Alignment.Center)
			)
			
			IconButton(
				onClick = {
					when {
						balanceFieldValue.text.isBlank() -> {
							context.getString(R.string.wallet_name_cannot_be_empty).toast(context)
						}
						else -> onSave(
							wallet.copy(
							
							)
						)
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
				
				balance = formattedValue.first
				balanceFieldValue = formattedValue.second
			},
			textStyle = LocalTextStyle.current.copy(
				fontFamily = Inter
			),
			keyboardOptions = KeyboardOptions(
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
					editBalanceOption = EditBalanceOption.ADD_TRANSACTION
				}
		) {
			RadioButton(
				selected = editBalanceOption == EditBalanceOption.ADD_TRANSACTION,
				onClick = {
					editBalanceOption = EditBalanceOption.ADD_TRANSACTION
				},
				modifier = Modifier
					.padding(
						horizontal = 8.dpScaled
					)
			)
			
			Text(
				text = stringResource(id = R.string.add_transaction),
				style = Typography.bodyMedium.copy(
					fontWeight = FontWeight.Normal,
					fontSize = Typography.bodyMedium.fontSize.spScaled
				)
			)
		}
		
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					vertical = 4.dpScaled
				)
				.clickable {
					editBalanceOption = EditBalanceOption.JUST_CHANGE
				}
		) {
			RadioButton(
				selected = editBalanceOption == EditBalanceOption.JUST_CHANGE,
				onClick = {
					editBalanceOption = EditBalanceOption.JUST_CHANGE
				},
				modifier = Modifier
					.padding(
						horizontal = 8.dpScaled
					)
			)
			
			Text(
				text = stringResource(id = R.string.add_transaction),
				style = Typography.bodyMedium.copy(
					fontWeight = FontWeight.Normal,
					fontSize = Typography.bodyMedium.fontSize.spScaled
				)
			)
		}
	}
}

private enum class EditBalanceOption {
	ADD_TRANSACTION,
	JUST_CHANGE
}
