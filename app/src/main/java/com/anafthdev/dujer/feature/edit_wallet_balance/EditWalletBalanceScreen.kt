package com.anafthdev.dujer.feature.edit_wallet_balance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.edit_wallet_balance.data.EditBalanceOption
import com.anafthdev.dujer.feature.theme.Inter
import com.anafthdev.dujer.feature.theme.Typography
import com.anafthdev.dujer.feature.theme.small_shape
import com.anafthdev.dujer.foundation.common.AppUtil.toast
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.common.TextFieldCurrencyFormatter
import com.anafthdev.dujer.foundation.extension.*
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import kotlin.random.Random

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditWalletBalanceScreen(
	navController: NavController,
	viewModel: EditWalletBalanceViewModel
) {
	
	val context = LocalContext.current
	val localCurrency = LocalCurrency.current
	val focusManager = LocalFocusManager.current
	
	val state by viewModel.state.collectAsStateWithLifecycle()
	
	val balanceFocusRequester = remember { FocusRequester() }
	
	LaunchedEffect(state.wallet?.initialBalance) {
		if (state.wallet != null) {
			viewModel.dispatch(
				EditWalletBalanceAction.ChangeBalance(
					amount = state.wallet!!.initialBalance,
					value = state.balanceFieldValue.copy(
						text = CurrencyFormatter.format(
							locale = deviceLocale,
							amount = state.wallet!!.initialBalance,
							useSymbol = false,
							currencyCode = localCurrency.countryCode
						)
					)
				)
			)
		}
	}
	
	Column(
		modifier = Modifier
			.imePadding()
			.systemBarsPadding()
			.fillMaxWidth()
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
		) {
			IconButton(
				onClick = {
					navController.popBackStack()
				},
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
						state.balanceFieldValue.text.isBlank() -> {
							context.getString(R.string.amount_cannot_be_empty).toast(context)
						}
						else -> {
							if (state.wallet != null) {
								val random = Random(System.currentTimeMillis())
								val id = random.nextInt()
								
								// amount - currentBalance
								val amount = state.balanceAmount - state.wallet!!.balance
								
								val mWallet = state.wallet!!.copy(
									initialBalance = if (state.editBalanceOption.isChangeInitialAmount()) state.balanceAmount
									else state.wallet!!.initialBalance
								)
								
								val financialType = if (amount >= 0) FinancialType.INCOME else FinancialType.EXPENSE
								
								val financial = Financial(
									id = id,
									name = "Transaction",
									amount = amount.toPositive(),
									type = financialType,
									walletID = mWallet.id,
									category = if (financialType.isIncome()) Category.incomeTransaction else Category.expenseTransaction,
									currency = localCurrency,
									dateCreated = System.currentTimeMillis()
								)
								
								viewModel.dispatch(
									EditWalletBalanceAction.Update(
										mWallet,
										if (state.editBalanceOption.isChangeBalance()) financial else null
									)
								)
							}
							
							navController.popBackStack()
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
			value = state.balanceFieldValue,
			onValueChange = { s ->
				val formattedValue = TextFieldCurrencyFormatter.getFormattedCurrency(
					fieldValue = s,
					countryCode = localCurrency.countryCode
				)
				
				viewModel.dispatch(
					EditWalletBalanceAction.ChangeBalance(
						amount = formattedValue.first,
						value = formattedValue.second
					)
				)
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
					viewModel.dispatch(
						EditWalletBalanceAction.ChangeBalanceOption(EditBalanceOption.CHANGE_BALANCE)
					)
				}
		) {
			RadioButton(
				selected = state.editBalanceOption.isChangeBalance(),
				onClick = {
					viewModel.dispatch(
						EditWalletBalanceAction.ChangeBalanceOption(EditBalanceOption.CHANGE_BALANCE)
					)
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
					visible = state.editBalanceOption.isChangeBalance()
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
					viewModel.dispatch(
						EditWalletBalanceAction.ChangeBalanceOption(EditBalanceOption.CHANGE_INITIAL_AMOUNT)
					)
				}
		) {
			RadioButton(
				selected = state.editBalanceOption.isChangeInitialAmount(),
				onClick = {
					viewModel.dispatch(
						EditWalletBalanceAction.ChangeBalanceOption(EditBalanceOption.CHANGE_INITIAL_AMOUNT)
					)
				},
				modifier = Modifier
					.padding(
						horizontal = 8.dpScaled
					)
			)
			
			Text(
				text = stringResource(id = R.string.change_starting_balance),
				style = Typography.bodyMedium.copy(
					color = LocalUiColor.current.titleText,
					fontWeight = FontWeight.Normal,
					fontSize = Typography.bodyMedium.fontSize.spScaled
				)
			)
		}
		
		Spacer(modifier = Modifier.height(16.dpScaled))
	}
}
