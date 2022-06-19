package com.anafthdev.dujer.ui.dashboard.subscreen

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
import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.WalletIcons
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.isLightTheme
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.CategoryTint
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.theme.*
import com.anafthdev.dujer.util.AppUtil.toast
import com.anafthdev.dujer.util.CurrencyFormatter
import com.anafthdev.dujer.util.TextFieldCurrencyFormatter
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import kotlin.random.Random

@Composable
fun AddWalletScreen(
	isScreenVisible: Boolean,
	walletNameFocusRequester: FocusRequester,
	onCancel: () -> Unit,
	onSave: (Wallet) -> Unit
) {
	
	val uiMode = LocalUiMode.current
	val localCurrency = LocalCurrency.current
	val context = LocalContext.current
	val focusManager = LocalFocusManager.current
	
	var walletName by remember { mutableStateOf("") }
	var walletTint by remember { mutableStateOf(CategoryTint.tint_1) }
	var walletIcon by remember { mutableStateOf(WalletIcons.WALLET) }
	var walletInitialBalance by remember { mutableStateOf(0.0) }
	
	var walletBalanceFieldValue by remember { mutableStateOf(TextFieldValue()) }
	
	var isSelectorWalletTintShowed by remember { mutableStateOf(false) }
	var isSelectorWalletIconShowed by remember { mutableStateOf(false) }
	
	val walletTintIconButtonRotation by animateFloatAsState(
		targetValue = if (isSelectorWalletTintShowed) 180f else 0f
	)
	val walletIconIconButtonRotation by animateFloatAsState(
		targetValue = if (isSelectorWalletIconShowed) 180f else 0f
	)
	
	LaunchedEffect(isScreenVisible) {
		walletName = ""
		walletTint = CategoryTint.tint_1
		walletIcon = WalletIcons.WALLET
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
						walletName.isBlank() -> {
							context.getString(R.string.wallet_name_cannot_be_empty).toast(context)
						}
						else -> onSave(
							Wallet(
								id = Random.nextInt(),
								name = walletName,
								initialBalance = walletInitialBalance,
								balance = walletInitialBalance,
								iconID = walletIcon,
								tint = walletTint,
								defaultWallet = false
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
				value = walletName,
				onValueChange = { s ->
					walletName = s
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
				value = walletBalanceFieldValue,
				onValueChange = { s ->
					val formattedValue = TextFieldCurrencyFormatter.getFormattedCurrency(
						fieldValue = s,
						countryCode = localCurrency.countryCode
					)
					
					walletInitialBalance = formattedValue.first
					walletBalanceFieldValue = formattedValue.second
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
								.background(walletTint.backgroundTint.toColor())
						)
						
						IconButton(
							onClick = {
								when {
									isSelectorWalletIconShowed and !isSelectorWalletTintShowed -> {
										isSelectorWalletTintShowed = true
										isSelectorWalletIconShowed = false
									}
									isSelectorWalletTintShowed -> {
										isSelectorWalletTintShowed = false
									}
									!isSelectorWalletTintShowed -> {
										isSelectorWalletTintShowed = true
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
							painter = painterResource(id = walletIcon),
							contentDescription = null,
							modifier = Modifier
								.size(28.dpScaled)
						)
						
						IconButton(
							onClick = {
								when {
									isSelectorWalletTintShowed and !isSelectorWalletIconShowed -> {
										isSelectorWalletIconShowed = true
										isSelectorWalletTintShowed = false
									}
									isSelectorWalletIconShowed -> {
										isSelectorWalletIconShowed = false
									}
									!isSelectorWalletIconShowed -> {
										isSelectorWalletIconShowed = true
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
				visible = isSelectorWalletTintShowed,
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
									walletTint = tint
								}
						)
					}
				}
			}
			
			AnimatedVisibility(
				visible = isSelectorWalletIconShowed,
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
									color = if (walletIcon == icon) MaterialTheme.colorScheme.primaryContainer
									else Color.Transparent
								)
								.border(
									width = 1.dpScaled,
									color = MaterialTheme.colorScheme.outline,
									shape = shapes.small
								)
								.clickable {
									walletIcon = icon
								}
						) {
							Icon(
								painter = painterResource(id = icon),
								tint = if ((walletIcon == icon) and uiMode.isLightTheme()) black01 else black10,
								contentDescription = null
							)
						}
					}
				}
			}
		}
	}
}
