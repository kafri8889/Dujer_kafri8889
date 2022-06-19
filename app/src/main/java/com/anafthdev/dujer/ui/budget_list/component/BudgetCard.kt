package com.anafthdev.dujer.ui.budget_list.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.roundToInt
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.theme.full_shape
import com.anafthdev.dujer.uicomponent.BudgetProgressIndicator
import com.anafthdev.dujer.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetCard(
	budget: Budget,
	expensesAmount: Double,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {

	Card(
		onClick = onClick,
		colors = CardDefaults.cardColors(
			containerColor = budget.category.tint.backgroundTint.toColor()
		),
		modifier = modifier
	) {
		Column(
			modifier = Modifier
				.padding(8.dpScaled)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(8.dpScaled)
					.fillMaxWidth()
			) {
				Icon(
					painter = painterResource(id = budget.category.iconID),
					contentDescription = null,
					tint = budget.category.tint.iconTint.toColor(),
					modifier = Modifier
						.padding(8.dpScaled)
						.size(28.dpScaled)
						.weight(0.15f)
				)
				
				Text(
					text = budget.category.name,
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyLarge.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.Medium,
						fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.65f)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
				
				Text(
					text = "${
						((expensesAmount / budget.max) * 100).roundToInt {
							((expensesAmount / budget.max) * 100).toInt()
						}
					}%",
					overflow = TextOverflow.Ellipsis,
					textAlign = TextAlign.End,
					style = MaterialTheme.typography.bodySmall.copy(
						color = LocalUiColor.current.labelText,
						fontWeight = FontWeight.Medium,
						fontSize = MaterialTheme.typography.bodySmall.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.2f)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
			}
			
			BudgetProgressIndicator(
				progress = (expensesAmount / budget.max).toFloat().coerceIn(
					minimumValue = 0f,
					maximumValue = 1f
				),
				stepColor = budget.category.tint.backgroundTint.toColor(),
				modifier = Modifier
					.padding(8.dpScaled)
					.fillMaxWidth()
					.clip(full_shape)
			)
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(
						vertical = 4.dpScaled,
						horizontal = 8.dpScaled
					)
					.fillMaxWidth()
			) {
				Text(
					text = stringResource(id = R.string.budget),
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = LocalUiColor.current.bodyText,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.4f)
				)
				
				Spacer(modifier = Modifier.weight(0.1f))
				
				Text(
					textAlign = TextAlign.End,
					text = CurrencyFormatter.format(
						locale = deviceLocale,
						amount = budget.max,
						currencyCode = LocalCurrency.current.countryCode
					),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = LocalUiColor.current.bodyText,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.5f)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
			}
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(
						vertical = 4.dpScaled,
						horizontal = 8.dpScaled
					)
					.fillMaxWidth()
			) {
				Text(
					text = stringResource(id = R.string.expenses),
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = LocalUiColor.current.bodyText,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.4f)
				)
				
				Spacer(modifier = Modifier.weight(0.1f))
				
				Text(
					textAlign = TextAlign.End,
					text = "-${
						CurrencyFormatter.format(
							locale = deviceLocale,
							amount = expensesAmount,
							currencyCode = LocalCurrency.current.countryCode
						)
					}",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = LocalUiColor.current.bodyText,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.5f)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
			}
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(
						vertical = 4.dpScaled,
						horizontal = 8.dpScaled
					)
					.fillMaxWidth()
			) {
				Text(
					text = stringResource(id = R.string.remaining),
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.Medium,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.4f)
				)
				
				Spacer(modifier = Modifier.weight(0.1f))
				
				Text(
					textAlign = TextAlign.End,
					text = CurrencyFormatter.format(
						locale = deviceLocale,
						amount = budget.max - expensesAmount,
						currencyCode = LocalCurrency.current.countryCode
					),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.Medium,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.5f)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
			}
		}
	}
}
