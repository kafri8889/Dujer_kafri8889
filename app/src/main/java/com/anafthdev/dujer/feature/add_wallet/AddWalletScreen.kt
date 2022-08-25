package com.anafthdev.dujer.feature.add_wallet

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.WalletIcons
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.app.data.LocalDujerController
import com.anafthdev.dujer.feature.theme.*
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.common.TextFieldCurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.isLightTheme
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.viewmodel.HandleEffect
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.CategoryTint
import com.anafthdev.dujer.model.LocalCurrency
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddWalletScreen(
	navController: NavController,
	viewModel: AddWalletViewModel
) {
	
	val uiMode = LocalUiMode.current
	val context = LocalContext.current
	val focusManager = LocalFocusManager.current
	val localCurrency = LocalCurrency.current
	val dujerController = LocalDujerController.current
	
	val state by viewModel.state.collectAsState()
	
	val (
		walletNameFocusRequester
	) = remember { FocusRequester.createRefs() }
	
	val walletTintIconButtonRotation by animateFloatAsState(
		targetValue = if (state.isSelectorWalletTintShowed) 180f else 0f
	)
	
	val walletIconIconButtonRotation by animateFloatAsState(
		targetValue = if (state.isSelectorWalletIconShowed) 180f else 0f
	)
	
	HandleEffect(
		viewModel = viewModel,
		handle = { effect ->
			when (effect) {
				is AddWalletEffect.BlankWalletName -> {
					dujerController.sendEffect(effect)
				}
				is AddWalletEffect.WalletCreated -> {
					dujerController.sendEffect(effect)
					navController.popBackStack()
				}
			}
		}
	)
	
	LaunchedEffect(Unit) {
		walletNameFocusRequester.requestFocus()
	}
	
	BackHandler {
		navController.popBackStack()
	}
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				if (uiMode.isLightTheme()) MaterialTheme.colorScheme.background
				else MaterialTheme.colorScheme.surfaceVariant
			)
			.verticalScroll(rememberScrollState())
			.imePadding()
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
				text = stringResource(id = R.string.new_wallet),
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
					if (viewModel.validateWalletName(state.name)) {
						viewModel.dispatch(
							AddWalletAction.Save(
								Wallet(
									id = Random.nextInt(),
									name = state.name,
									initialBalance = state.initialBalance,
									balance = state.initialBalance,
									iconID = state.icon,
									tint = state.tint,
									defaultWallet = false
								)
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
		
		Column(
			modifier = Modifier
				.padding(
					horizontal = 16.dpScaled
				)
		) {
			Text(
				text = stringResource(id = R.string.name),
				style = Typography.titleMedium.copy(
					color = LocalUiColor.current.titleText,
					fontWeight = FontWeight.Medium,
					fontSize = Typography.titleMedium.fontSize.spScaled
				),
				modifier = Modifier
					.padding(
						top = 16.dpScaled
					)
			)
			
			OutlinedTextField(
				singleLine = true,
				shape = small_shape,
				value = state.name,
				onValueChange = { name ->
					viewModel.dispatch(
						AddWalletAction.ChangeName(name)
					)
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
				modifier = Modifier
					.padding(
						top = 8.dpScaled,
						bottom = 16.dpScaled
					)
					.fillMaxWidth()
			)
			
			Text(
				text = stringResource(id = R.string.initial_balance),
				style = Typography.titleMedium.copy(
					color = LocalUiColor.current.titleText,
					fontWeight = FontWeight.Medium,
					fontSize = Typography.titleMedium.fontSize.spScaled
				),
				modifier = Modifier
			
			)
			
			OutlinedTextField(
				singleLine = true,
				shape = small_shape,
				value = state.balanceFieldValue,
				onValueChange = { s ->
					val formattedValue = TextFieldCurrencyFormatter.getFormattedCurrency(
						fieldValue = s,
						countryCode = localCurrency.countryCode
					)
					
					viewModel.dispatches(
						AddWalletAction.ChangeInitialBalance(formattedValue.first),
						AddWalletAction.ChangeBalanceFieldValue(formattedValue.second)
					)
				},
				textStyle = LocalTextStyle.current.copy(
					fontFamily = Inter
				),
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
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				),
				modifier = Modifier
					.padding(
						top = 8.dpScaled,
						bottom = 16.dpScaled
					)
					.fillMaxWidth()
					.focusRequester(walletNameFocusRequester)
			)
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						bottom = 24.dpScaled
					)
			) {
				Column(
					verticalArrangement = Arrangement.Center,
					modifier = Modifier
						.padding()
						.weight(0.6f)
				) {
					Text(
						text = stringResource(id = R.string.color),
						style = Typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.Medium,
							fontSize = Typography.titleMedium.fontSize.spScaled
						)
					)
					
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.padding(
								top = 4.dpScaled
							)
							.fillMaxWidth()
							.clip(medium_shape)
							.background(md_theme_dark_inverseOnSurface)
					) {
						Box(
							modifier = Modifier
								.padding(
									start = 8.dpScaled
								)
								.weight(1f)
								.height(24.dpScaled)
								.clip(MaterialTheme.shapes.small)
								.background(state.tint.backgroundTint.toColor())
						)
						
						IconButton(
							onClick = {
								when {
									state.isSelectorWalletIconShowed and !state.isSelectorWalletTintShowed -> {
										viewModel.dispatches(
											AddWalletAction.SetIsSelectorWalletTintShowed(true),
											AddWalletAction.SetIsSelectorWalletIconShowed(false)
										)
									}
									state.isSelectorWalletTintShowed -> {
										viewModel.dispatch(
											AddWalletAction.SetIsSelectorWalletTintShowed(false)
										)
									}
									!state.isSelectorWalletTintShowed -> {
										viewModel.dispatch(
											AddWalletAction.SetIsSelectorWalletTintShowed(true)
										)
									}
								}
							},
							modifier = Modifier
								.padding(4.dpScaled)
						) {
							Icon(
								imageVector = Icons.Rounded.ArrowDropDown,
								tint = black10,
								contentDescription = null,
								modifier = Modifier
									.rotate(walletTintIconButtonRotation)
							)
						}
					}
				}
				
				Spacer(
					modifier = Modifier
						.weight(0.1f)
				)
				
				Column(
					verticalArrangement = Arrangement.Center,
					modifier = Modifier
						.weight(0.3f)
				) {
					Text(
						text = stringResource(id = R.string.icon),
						style = Typography.titleMedium.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.Medium,
							fontSize = Typography.titleMedium.fontSize.spScaled
						)
					)
					
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.padding(
								top = 4.dpScaled
							)
							.fillMaxWidth()
					) {
						Icon(
							painter = painterResource(id = state.icon),
							contentDescription = null,
							modifier = Modifier
								.size(28.dpScaled)
						)
						
						IconButton(
							onClick = {
								when {
									state.isSelectorWalletTintShowed and !state.isSelectorWalletIconShowed -> {
										viewModel.dispatches(
											AddWalletAction.SetIsSelectorWalletTintShowed(false),
											AddWalletAction.SetIsSelectorWalletIconShowed(true)
										)
									}
									state.isSelectorWalletIconShowed -> {
										viewModel.dispatch(
											AddWalletAction.SetIsSelectorWalletIconShowed(false)
										)
									}
									!state.isSelectorWalletIconShowed -> {
										viewModel.dispatch(
											AddWalletAction.SetIsSelectorWalletIconShowed(true)
										)
									}
								}
							},
							modifier = Modifier
								.padding(4.dpScaled)
						) {
							Icon(
								imageVector = Icons.Rounded.ArrowDropDown,
								contentDescription = null,
								modifier = Modifier
									.rotate(walletIconIconButtonRotation)
							)
						}
					}
				}
			}
			
			AnimatedVisibility(
				visible = state.isSelectorWalletTintShowed,
				enter = expandVertically(
					animationSpec = tween(400)
				),
				exit = shrinkVertically(
					animationSpec = tween(300)
				),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						bottom = 24.dpScaled
					)
			) {
				FlowRow(
					mainAxisAlignment = FlowMainAxisAlignment.Center,
					crossAxisAlignment = FlowCrossAxisAlignment.Center,
					modifier = Modifier
						.clip(MaterialTheme.shapes.medium)
						.background(md_theme_dark_inverseOnSurface)
						.padding(8.dpScaled)
				) {
					for (tint in CategoryTint.values) {
						Box(
							modifier = Modifier
								.padding(8.dpScaled)
								.size(28.dpScaled)
								.clip(full_shape)
								.background(tint.backgroundTint.toColor())
								.clickable {
									viewModel.dispatch(
										AddWalletAction.ChangeTint(tint)
									)
								}
						)
					}
				}
			}
			
			AnimatedVisibility(
				visible = state.isSelectorWalletIconShowed,
				enter = expandVertically(
					animationSpec = tween(400)
				),
				exit = shrinkVertically(
					animationSpec = tween(300)
				),
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						bottom = 24.dpScaled
					)
			) {
				FlowRow(
					mainAxisAlignment = FlowMainAxisAlignment.Center,
					crossAxisAlignment = FlowCrossAxisAlignment.Center,
					modifier = Modifier
						.clip(MaterialTheme.shapes.medium)
						.background(md_theme_dark_inverseOnSurface)
						.padding(8.dpScaled)
				) {
					for (icon in WalletIcons.values) {
						Box(
							contentAlignment = Alignment.Center,
							modifier = Modifier
								.padding(4.dpScaled)
								.size(48.dpScaled)
								.clip(shapes.small)
								.background(
									color = if (state.icon == icon) MaterialTheme.colorScheme.primaryContainer
									else Color.Transparent
								)
								.border(
									width = 1.dpScaled,
									color = MaterialTheme.colorScheme.outline,
									shape = shapes.small
								)
								.clickable {
									viewModel.dispatch(
										AddWalletAction.ChangeIcon(icon)
									)
								}
						) {
							Icon(
								painter = painterResource(id = icon),
								tint = if (state.icon == icon && uiMode.isLightTheme()) black01 else black10,
								contentDescription = null
							)
						}
					}
				}
			}
		}
	}
}
