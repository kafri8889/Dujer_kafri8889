package com.anafthdev.dujer.feature.statistic.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.anafthdev.dujer.R
import com.anafthdev.dujer.feature.theme.expense_color
import com.anafthdev.dujer.feature.theme.income_color
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceSummaryCard(
	totalIncome: Double,
	totalExpense: Double,
	modifier: Modifier = Modifier
) {
	
	val totalIncomeExpense = remember(totalIncome, totalExpense) {
		totalIncome - totalExpense
	}
	
	Card(
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
				text = stringResource(id = R.string.summary),
				style = MaterialTheme.typography.titleMedium.copy(
					color = LocalUiColor.current.titleText,
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
						top = 8.dpScaled
					)
			) {
				Text(
					text = stringResource(id = R.string.income),
					textAlign = TextAlign.Start,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = LocalUiColor.current.titleText,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.4f)
				)
				
				Text(
					text = "+${
						CurrencyFormatter.format(
							locale = deviceLocale,
							amount = totalIncome,
							currencyCode = LocalCurrency.current.countryCode
						)
					}",
					textAlign = TextAlign.End,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = income_color,
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
						top = 8.dpScaled
					)
			) {
				Text(
					text = stringResource(id = R.string.expenses),
					textAlign = TextAlign.Start,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.4f)
				)
				
				Text(
					text = "-${
						CurrencyFormatter.format(
							locale = deviceLocale,
							amount = totalExpense,
							currencyCode = LocalCurrency.current.countryCode
						)
					}",
					textAlign = TextAlign.End,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = expense_color,
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
						vertical = 8.dpScaled
					)
			) {
				Text(
					text = stringResource(id = R.string.total),
					textAlign = TextAlign.Start,
					style = MaterialTheme.typography.titleSmall.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.Medium,
						fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.4f)
				)
				
				Text(
					text = CurrencyFormatter.format(
						locale = deviceLocale,
						amount = totalIncomeExpense,
						currencyCode = LocalCurrency.current.countryCode
					),
					textAlign = TextAlign.End,
					style = MaterialTheme.typography.titleSmall.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.Medium,
						fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
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
