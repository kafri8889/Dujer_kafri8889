package com.anafthdev.dujer.ui.dashboard.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.R
import com.anafthdev.dujer.foundation.extension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.balance_card_background
import com.anafthdev.dujer.ui.theme.large_shape
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceCard(
	balance: Double,
) {
	
	val density = LocalDensity.current
	
	var cardHeight by remember { mutableStateOf(0.dpScaled) }
	
	Card(
		shape = large_shape,
		elevation = CardDefaults.cardElevation(
			defaultElevation = 4.dp
		),
		colors = CardDefaults.cardColors(
			containerColor = balance_card_background
		),
		modifier = Modifier
			.fillMaxWidth()
			.onGloballyPositioned {
				cardHeight = with(density) {
					it.size.width
						.div(2f)
						.toDp()
				}
			}
			.height(cardHeight)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(
					vertical = 24.dpScaled,
					horizontal = 16.dp
				)
		) {
			Text(
				text = stringResource(id = R.string.available_balance),
				style = Typography.bodyMedium.copy(
					color = Color.White,
					fontWeight = FontWeight.Normal
				)
			)
			
			Text(
				text = CurrencyFormatter.format(
					locale = AppUtil.deviceLocale,
					amount = balance,
					useSymbol = true,
					currencyCode = LocalCurrency.current.countryCode
				),
				style = Typography.headlineMedium.copy(
					color = Color.White,
					fontWeight = FontWeight.SemiBold
				),
				modifier = Modifier
					.padding(top = 16.dpScaled)
					.horizontalScroll(
						state = rememberScrollState(),
						autoRestart = true
					)
			)
			
			Spacer(
				modifier = Modifier
					.weight(1f)
			)
			
			Text(
				text = stringResource(id = R.string.see_details),
				style = Typography.bodyMedium.copy(
					color = Color.White,
					fontWeight = FontWeight.SemiBold
				)
			)
		}
	}
}
