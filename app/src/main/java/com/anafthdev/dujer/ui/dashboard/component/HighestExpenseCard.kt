package com.anafthdev.dujer.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.theme.large_shape
import com.anafthdev.dujer.ui.theme.md_theme_light_secondaryContainer
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighestExpenseCard(
	category: Category,
	totalExpense: Double,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	
	Card(
		shape = large_shape,
		elevation = CardDefaults.cardElevation(
			defaultElevation = 0.dp
		),
		colors = CardDefaults.cardColors(
			containerColor = md_theme_light_secondaryContainer
		),
		onClick = onClick,
		modifier = modifier
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier
				.padding(12.dpScaled)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
			) {
				Text(
					text = stringResource(id = R.string.highest_expense),
					style = MaterialTheme.typography.titleSmall.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.SemiBold,
						fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.8f)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
				
				Text(
					textAlign = TextAlign.End,
					text = AppUtil.longMonths[Calendar.getInstance()[Calendar.MONTH]],
					style = MaterialTheme.typography.titleSmall.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.SemiBold,
						fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.2f)
				)
			}
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dpScaled)
					.background(
						color = MaterialTheme.colorScheme.background,
						shape = MaterialTheme.shapes.large
					)
					.padding(
						horizontal = 8.dpScaled,
						vertical = 16.dpScaled
					)
			) {
				Icon(
					painter = painterResource(id = category.iconID),
					contentDescription = null,
					tint = category.tint.iconTint.toColor(),
					modifier = Modifier
						.size(32.dpScaled)
						.weight(0.16f)
				)
				
				Text(
					maxLines = 1,
					text = category.name,
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = LocalUiColor.current.titleText,
						fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.35f)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
				
				Spacer(modifier = Modifier.weight(0.02f))
				
				Text(
					maxLines = 1,
					textAlign = TextAlign.End,
					text = CurrencyFormatter.format(
						locale = deviceLocale,
						amount = totalExpense,
						currencyCode = LocalCurrency.current.countryCode
					),
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.titleSmall.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.SemiBold,
						fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
					),
					modifier = Modifier
						.weight(0.45f)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
				
				Spacer(modifier = Modifier.weight(0.02f))
			}
		}
	}
}
