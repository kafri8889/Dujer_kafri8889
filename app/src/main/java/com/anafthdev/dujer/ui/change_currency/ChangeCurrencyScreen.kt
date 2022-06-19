package com.anafthdev.dujer.ui.change_currency

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.change_currency.component.SelectCurrencyItem
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.semi_large_shape
import com.anafthdev.dujer.uicomponent.TopAppBar

@Composable
fun ChangeCurrencyScreen(
	navController: NavController
) {
	
	val focusManager = LocalFocusManager.current
	
	val changeCurrencyViewModel = hiltViewModel<ChangeCurrencyViewModel>()
	
	val state by changeCurrencyViewModel.state.collectAsState()
	
	val resultCurrency = state.resultCurrency
	
	var searchText by remember { mutableStateOf("") }
	
	BackHandler {
		navController.popBackStack()
	}
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.systemBarsPadding()
	) {
		TopAppBar {
			IconButton(
				onClick = {
					navController.popBackStack()
				},
				modifier = Modifier
					.padding(start = 8.dpScaled)
					.align(Alignment.CenterStart)
			) {
				Icon(
					imageVector = Icons.Rounded.ArrowBack,
					contentDescription = null
				)
			}
			
			Text(
				text = stringResource(id = R.string.change_currency),
				style = MaterialTheme.typography.titleLarge.copy(
					color = LocalUiColor.current.headlineText,
					fontWeight = FontWeight.Bold,
					fontSize = Typography.titleLarge.fontSize.spScaled
				)
			)
		}
		
		OutlinedTextField(
			shape = semi_large_shape,
			singleLine = true,
			value = searchText,
			onValueChange = { s ->
				searchText = s
				changeCurrencyViewModel.dispatch(
					ChangeCurrencyAction.Search(searchText)
				)
			},
			textStyle = LocalTextStyle.current.copy(
				fontFamily = Inter
			),
			leadingIcon = {
				Icon(
					imageVector = Icons.Rounded.Search,
					contentDescription = null
				)
			},
			keyboardOptions = KeyboardOptions(
				imeAction = ImeAction.Done
			),
			keyboardActions = KeyboardActions(
				onDone = {
					focusManager.clearFocus(force = true)
				}
			),
			modifier = Modifier
				.padding(8.dpScaled)
				.fillMaxWidth()
		)
		
		LazyColumn {
			items(
				items = resultCurrency.ifEmpty { Currency.availableCurrency },
				key = { item: Currency -> item.countryCode }
			) { currency ->
				SelectCurrencyItem(
					selected = LocalCurrency.current.countryCode == currency.countryCode,
					currency = currency,
					onSelected = {
						changeCurrencyViewModel.dispatch(
							ChangeCurrencyAction.ChangeCurrency(currency)
						)
					}
				)
			}
		}
	}
}
