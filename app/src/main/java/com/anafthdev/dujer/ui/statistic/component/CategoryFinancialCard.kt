package com.anafthdev.dujer.ui.statistic.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.uicomponent.DashedDivider
import com.anafthdev.dujer.util.CurrencyFormatter

@Composable
fun CategoryFinancialCard(
	category: Category,
	percent: String,
	totalAmount: Double,
	financialList: List<Financial>,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	
	Column(
		modifier = modifier
			.fillMaxWidth()
			.clickable(
				enabled = true,
				onClickLabel = null,
				role = Role.Button,
				onClick = onClick
			)
			.padding(
				horizontal = 16.dpScaled
			)
	) {
		DashedDivider(
			thickness = 1.dpScaled,
			modifier = Modifier
				.fillMaxWidth()
		)
		
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					vertical = 16.dpScaled
				)
		) {
			Icon(
				painter = painterResource(id = category.iconID),
				contentDescription = null,
				tint = category.tint.iconTint.toColor(),
				modifier = Modifier
					.padding(8.dpScaled)
					.size(32.dpScaled)
			)
			
			Column(
				verticalArrangement = Arrangement.Center,
				modifier = Modifier
					.fillMaxWidth()
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(
						maxLines = 2,
						text = category.name,
						overflow = TextOverflow.Ellipsis,
						style = Typography.bodyMedium.copy(
							fontSize = Typography.bodyMedium.fontSize.spScaled
						),
						modifier = Modifier
							.weight(0.45f)
					)
					
					Text(
						maxLines = 1,
						textAlign = TextAlign.End,
						text = CurrencyFormatter.format(
							locale = deviceLocale,
							amount = totalAmount,
							currencyCode = LocalCurrency.current.countryCode
						),
						overflow = TextOverflow.Ellipsis,
						style = Typography.bodyMedium.copy(
							fontWeight = FontWeight.SemiBold,
							fontSize = Typography.bodyMedium.fontSize.spScaled
						),
						modifier = Modifier
							.weight(0.55f)
							.horizontalScroll(
								state = rememberScrollState(),
								autoRestart = true
							)
					)
				}
				
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.padding(top = 4.dpScaled)
						.fillMaxWidth()
				) {
					Text(
						maxLines = 1,
						text = "$percent%",
						overflow = TextOverflow.Ellipsis,
						style = Typography.bodySmall.copy(
							fontSize = Typography.bodySmall.fontSize.spScaled
						),
						modifier = Modifier
							.weight(0.4f)
					)
					
					Text(
						maxLines = 2,
						text = stringResource(
							id = R.string.n_transaction,
							financialList.size.toString()
						),
						textAlign = TextAlign.End,
						overflow = TextOverflow.Ellipsis,
						style = Typography.bodySmall.copy(
							fontSize = Typography.bodySmall.fontSize.spScaled
						),
						modifier = Modifier
							.weight(0.6f)
					)
				}
			}
		}
	}
}
