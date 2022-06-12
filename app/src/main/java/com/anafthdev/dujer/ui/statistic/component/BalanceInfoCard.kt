package com.anafthdev.dujer.ui.statistic.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.anafthdev.dujer.R
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceInfoCard(
	initialBalance: Double,
	currentBalance: Double,
	modifier: Modifier = Modifier
) {
	
	Card(
		elevation = CardDefaults.cardElevation(
			defaultElevation = 1.dpScaled
		),
		modifier = modifier
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					horizontal = 8.dpScaled
				)
		) {
			Text(
				text = stringResource(id = R.string.balance),
				style = MaterialTheme.typography.titleMedium.copy(
					fontWeight = FontWeight.SemiBold,
					fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
				),
				modifier = Modifier
					.padding(
						top = 8.dpScaled
					)
			)
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						vertical = 8.dpScaled
					)
			) {
				Text(
					text = stringResource(id = R.string.initial_balance),
					textAlign = TextAlign.Start,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.4f)
				)
				
				Text(
					text = CurrencyFormatter.format(
						locale = deviceLocale,
						amount = initialBalance,
						currencyCode = LocalCurrency.current.countryCode
					),
					textAlign = TextAlign.End,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.6f)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
			}
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						bottom = 8.dpScaled
					)
			) {
				Text(
					text = stringResource(id = R.string.current_balance),
					textAlign = TextAlign.Start,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.4f)
				)
				
				Text(
					text = CurrencyFormatter.format(
						locale = deviceLocale,
						amount = currentBalance,
						currencyCode = LocalCurrency.current.countryCode
					),
					textAlign = TextAlign.End,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.6f)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
			}
		}
	}
}
