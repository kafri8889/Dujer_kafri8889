package com.anafthdev.dujer.ui.budget_list.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.uicomponent.BudgetProgressIndicator
import com.anafthdev.dujer.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetCard(
	budget: Budget,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {

	Card(
		onClick = onClick,
		colors = CardDefaults.cardColors(
			containerColor = budget.tint.backgroundTint.toColor()
		),
		modifier = modifier
	) {
		Column {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(8.dpScaled)
					.fillMaxWidth()
			) {
				Icon(
					painter = painterResource(id = budget.category.iconID),
					contentDescription = null,
					tint = budget.tint.iconTint.toColor(),
					modifier = Modifier
						.padding(8.dpScaled)
						.size(28.dpScaled)
				)
				
				Text(
					text = budget.category.name,
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyLarge.copy(
						fontWeight = FontWeight.Medium,
						fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
					),
					modifier = Modifier
						.weight(1f)
				)
			}
			
			BudgetProgressIndicator(
				progress = 0.5f,
				stepColor = budget.tint.backgroundTint.toColor(),
				modifier = Modifier
					.fillMaxWidth()
			)
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
			) {
				Text(
					text = stringResource(id = R.string.budget),
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyMedium.copy(
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.4f)
				)
				
				Spacer(modifier = Modifier.weight(0.1f))
				
				Text(
					text = CurrencyFormatter.format(
						locale = deviceLocale,
						amount = budget.max,
						currencyCode = LocalCurrency.current.countryCode
					),
					style = MaterialTheme.typography.bodyMedium.copy(
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
