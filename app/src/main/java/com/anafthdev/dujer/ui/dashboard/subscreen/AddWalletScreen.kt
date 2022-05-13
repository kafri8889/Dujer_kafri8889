package com.anafthdev.dujer.ui.dashboard.subscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.WalletIcons
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.CategoryTint
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.uicomponent.OutlinedTextField

@Composable
fun AddWalletScreen(
	isScreenVisible: Boolean,
	walletNameFocusRequester: FocusRequester,
	onCancel: () -> Unit,
	onSave: (Wallet) -> Unit
) {
	
	val focusManager = LocalFocusManager.current
	
	var walletName by remember { mutableStateOf("") }
	var walletTint by remember { mutableStateOf(CategoryTint.tint_1) }
	var walletIcon by remember { mutableStateOf(WalletIcons.WALLET) }
	
	LaunchedEffect(isScreenVisible) {
		walletName = ""
		walletTint = CategoryTint.tint_1
		walletIcon = WalletIcons.WALLET
	}
	
	Column(
		modifier = Modifier
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
				style = Typography.bodyLarge.copy(
					fontWeight = FontWeight.Bold,
					fontSize = Typography.bodyLarge.fontSize.spScaled
				),
				modifier = Modifier
					.align(Alignment.Center)
			)
			
			IconButton(
				onClick = {
				
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
		
		Text(
			text = "Name",
			style = Typography.bodyLarge.copy(
				fontWeight = FontWeight.Medium,
				fontSize = Typography.bodyLarge.fontSize.spScaled
			),
			modifier = Modifier
				.padding(
					top = 16.dpScaled,
					end = 16.dpScaled,
					start = 16.dpScaled
				)
		)
		
		OutlinedTextField(
			singleLine = true,
			maxCounter = 30,
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
					end = 16.dpScaled,
					start = 16.dpScaled,
					bottom = 16.dpScaled
				)
				.fillMaxWidth()
				.focusRequester(walletNameFocusRequester)
		)
	}
}
