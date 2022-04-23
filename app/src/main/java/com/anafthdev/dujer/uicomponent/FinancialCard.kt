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
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.large_shape
import com.anafthdev.dujer.ui.theme.medium_shape
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter
import java.text.SimpleDateFormat
import java.util.*

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
						style = Typography.bodyMedium.copy(
							fontWeight = FontWeight.SemiBold,
							fontSize = Typography.bodyMedium.fontSize.spScaled
						)
					)
					
					Text(
						text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(financial.dateCreated),
						style = Typography.labelSmall.copy(
							fontWeight = FontWeight.Normal,
							fontSize = Typography.labelSmall.fontSize.spScaled
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
						text = (if (financial.type == FinancialType.INCOME) "+" else "-") +
								CurrencyFormatter.format(
									locale = AppUtil.deviceLocale,
									amount = financial.amount,
									currencyCode = LocalCurrency.current.countryCode
								),
						style = Typography.bodyMedium.copy(
							fontWeight = FontWeight.SemiBold,
							fontSize = Typography.bodyMedium.fontSize.spScaled
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
						style = Typography.labelSmall.copy(
							fontWeight = FontWeight.Normal,
							fontSize = Typography.labelSmall.fontSize.spScaled
						),
						modifier = Modifier
							.padding(top = 8.dpScaled)
					)
				}
			}
		}
	}
}
