package com.anafthdev.dujer.ui.screen.dashboard.component

import androidx.compose.foundation.layout.*
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
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.balance_card_background
import com.anafthdev.dujer.ui.theme.big_shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceCard(
	balance: Double,
	currency: Currency
) {
	
	val density = LocalDensity.current
	
	var cardHeight by remember { mutableStateOf(0.dpScaled) }
	
	Card(
		shape = big_shape,
		containerColor = balance_card_background,
		elevation = CardDefaults.cardElevation(
			defaultElevation = 4.dp
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
				text = "${currency.symbol} $balance",
				style = Typography.headlineMedium.copy(
					color = Color.White,
					fontWeight = FontWeight.SemiBold
				),
				modifier = Modifier
					.padding(top = 16.dpScaled)
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
