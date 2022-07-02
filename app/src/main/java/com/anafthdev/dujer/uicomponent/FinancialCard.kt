package com.anafthdev.dujer.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.common.AppUtil.dateFormatter
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.theme.large_shape
import com.anafthdev.dujer.ui.theme.medium_shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialCard(
	financial: Financial,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	
	Card(
		shape = large_shape,
		onClick = onClick,
		elevation = CardDefaults.cardElevation(
			defaultElevation = 1.dp
		),
		colors = CardDefaults.cardColors(
			containerColor = if (LocalUiMode.current.isDarkTheme()) CardDefaults.cardColors().containerColor(true).value
			else Color.White
		),
		modifier = modifier
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dpScaled)
		) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.size(64.dpScaled)
					.clip(medium_shape)
					.background(financial.category.tint.backgroundTint.toColor())
			) {
				Icon(
					painter = painterResource(id = financial.category.iconID),
					tint = financial.category.tint.iconTint.toColor(),
					contentDescription = null
				)
			}
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(start = 8.dpScaled)
					.fillMaxSize()
					.height(64.dpScaled)
			) {
				Column(
					modifier = Modifier
						.weight(0.45f)
				) {
					Text(
						text = financial.name,
						maxLines = 2,
						style = MaterialTheme.typography.titleSmall.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.SemiBold,
							fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
						)
					)
					
					Text(
						text = dateFormatter.format(financial.dateCreated),
						style = MaterialTheme.typography.labelSmall.copy(
							color = LocalUiColor.current.bodyText,
							fontWeight = FontWeight.Normal,
							fontSize = MaterialTheme.typography.labelSmall.fontSize.spScaled
						),
						modifier = Modifier
							.padding(top = 8.dpScaled)
					)
				}
				
				Column(
					horizontalAlignment = Alignment.End,
					modifier = Modifier
						.weight(0.55f)
				) {
					Text(
						text = "${if (financial.type == FinancialType.INCOME) " + " else " - "}${
							CurrencyFormatter.format(
								locale = deviceLocale,
								amount = financial.amount,
								currencyCode = LocalCurrency.current.countryCode
							)
						}",
						style = MaterialTheme.typography.titleSmall.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.SemiBold,
							fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
						),
						modifier = Modifier
							.horizontalScroll(
								state = rememberScrollState(),
								autoRestart = true
							)
					)
					
					Text(
						text = financial.category.name,
						maxLines = 1,
						overflow = TextOverflow.Ellipsis,
						style = MaterialTheme.typography.labelSmall.copy(
							color = LocalUiColor.current.bodyText,
							fontWeight = FontWeight.Normal,
							fontSize = MaterialTheme.typography.labelSmall.fontSize.spScaled
						),
						modifier = Modifier
							.padding(top = 8.dpScaled)
					)
				}
			}
		}
	}
}
