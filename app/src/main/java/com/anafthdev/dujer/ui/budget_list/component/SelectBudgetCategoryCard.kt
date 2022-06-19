package com.anafthdev.dujer.ui.budget_list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.extension.darkenColor
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.md_theme_light_onSurface
import com.anafthdev.dujer.ui.theme.md_theme_light_primary
import com.anafthdev.dujer.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBudgetCategoryCard(
	selected: Boolean,
	category: Category,
	averagePerMonth: Double,
	onClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	
	Card(
		onClick = onClick,
		colors = CardDefaults.cardColors(
			containerColor = category.tint.backgroundTint
				.toColor()
				.darkenColor(
					moreThanLuminance = 85f,
					factor = 0.03f
				)
		),
		border = BorderStroke(
			width = 1.dpScaled,
			color = if (selected) md_theme_light_primary
			else Color.Transparent
		),
		modifier = modifier
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.padding(8.dpScaled)
				.fillMaxWidth()
		) {
			Icon(
				painter = painterResource(id = category.iconID),
				contentDescription = null,
				tint = category.tint.iconTint.toColor(),
				modifier = Modifier
					.padding(8.dpScaled)
			)
			
			Column(
				verticalArrangement = Arrangement.Center,
				modifier = Modifier
					.weight(1f)
			) {
				Text(
					text = category.name,
					style = Typography.bodyMedium.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.Medium,
						fontSize = Typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.fillMaxWidth()
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
				
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(
						text = stringResource(id = R.string.average_per_month),
						style = Typography.bodySmall.copy(
							color = LocalUiColor.current.bodyText,
							fontSize = Typography.bodySmall.fontSize.spScaled
						),
						modifier = Modifier
							.weight(0.4f)
					)
					
					Spacer(modifier = Modifier.weight(0.1f))
					
					Text(
						maxLines = 1,
						textAlign = TextAlign.End,
						text = CurrencyFormatter.format(
							locale = deviceLocale,
							amount = averagePerMonth,
							currencyCode = LocalCurrency.current.countryCode
						),
						style = Typography.bodySmall.copy(
							color = LocalUiColor.current.bodyText,
							fontSize = Typography.bodySmall.fontSize.spScaled
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
			
			RadioButton(
				selected = selected,
				onClick = onClick,
				colors = RadioButtonDefaults.colors(
					selectedColor = md_theme_light_primary,
					unselectedColor = md_theme_light_onSurface
				)
			)
		}
	}
}
