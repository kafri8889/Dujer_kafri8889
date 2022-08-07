package com.anafthdev.dujer.feature.financial.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.theme.medium_shape
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.foundation.extension.isLightTheme
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled

@Composable
fun MaxBudgetReachedPopup(
	budget: Budget,
	financial: Financial,
	financialEdit: Financial,
	onClose: () -> Unit,
	onIgnore: () -> Unit
) {
	
	val uiMode = LocalUiMode.current
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = Modifier
			.fillMaxSize()
			.background(
				if (uiMode.isLightTheme()) Color.Black.copy(alpha = 0.24f)
				else Color.White.copy(alpha = 0.24f)
			)
			.clickable(
				enabled = true,
				interactionSource = MutableInteractionSource(),
				indication = null,
				onClick = onClose
			)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					horizontal = 24.dpScaled
				)
				.clip(medium_shape)
				.background(MaterialTheme.colorScheme.background)
				.padding(16.dpScaled)
				.clickable(
					enabled = true,
					interactionSource = MutableInteractionSource(),
					indication = null,
					onClick = {}
				)
		) {
			Text(
				text = stringResource(id = R.string.budget_reaches_limit),
				style = MaterialTheme.typography.bodyLarge.copy(
					fontWeight = FontWeight.SemiBold,
					fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
				),
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
			)
			
			Text(
				text = buildAnnotatedString {
					val amount = financial.amount - financialEdit.amount
					val remainingAmount = budget.remaining - amount
					val formattedRemaining = CurrencyFormatter.format(
						locale = deviceLocale,
						amount = remainingAmount,
						useSymbol = true,
						currencyCode = financial.currency.countryCode
					)
					
					val s = stringResource(
						id = R.string.budget_reaches_limit_message,
						formattedRemaining
					)
					
					val (startIndex, endIndex) = s.indexOf(formattedRemaining)
					
					withStyle(
						MaterialTheme.typography.bodyMedium.copy(
							fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
						).toSpanStyle()
					) {
						append(s)
					}
					
					addStyle(
						style = MaterialTheme.typography.titleSmall.copy(
							color = LocalUiColor.current.titleText,
							fontWeight = FontWeight.SemiBold,
							fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
						).toSpanStyle(),
						start = startIndex,
						end = endIndex + 1
					)
				},
				style = MaterialTheme.typography.bodyMedium.copy(
					fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
				),
				modifier = Modifier
					.padding(
						top = 8.dpScaled
					)
			)
			
			Row(
				horizontalArrangement = Arrangement.End,
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dpScaled)
			) {
				TextButton(
					shape = MaterialTheme.shapes.medium,
					onClick = onClose
				) {
					Text(
						text = stringResource(id = R.string.cancel)
					)
				}
				
				Button(
					shape = MaterialTheme.shapes.medium,
					onClick = onIgnore,
					modifier = Modifier
						.padding(
							horizontal = 8.dpScaled
						)
				) {
					Text(
						text = stringResource(id = R.string.ignore),
						style = LocalTextStyle.current.copy(
							color = Color.White
						)
					)
				}
			}
		}
	}
}
