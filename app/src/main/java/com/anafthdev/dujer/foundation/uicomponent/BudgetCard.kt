package com.anafthdev.dujer.foundation.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.R
import com.anafthdev.dujer.feature.theme.*
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.extension.isLightTheme
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import timber.log.Timber
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetCard(
	totalExpense: Double,
	totalBudget: Double,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	
	val uiMode = LocalUiMode.current
	
	val indicatorProgress = remember(totalExpense, totalBudget) {
		try {
			val progress = totalExpense.toFloat() / totalBudget.toFloat()
			
			if (progress.isNaN()) 0f else progress
		} catch (e: IllegalArgumentException) {
			Timber.e(e)
			0f
		}
	}
	
	val remainingBudget = remember(totalExpense, totalBudget) {
		totalBudget - totalExpense
	}
	
	Card(
		shape = large_shape,
		elevation = CardDefaults.cardElevation(
			defaultElevation = 0.dp
		),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.tertiaryContainer
		),
		onClick = onClick,
		modifier = modifier
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier
				.padding(16.dpScaled)
		) {
			CompositionLocalProvider(
				LocalContentColor provides if (uiMode.isLightTheme()) black02 else black09
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(
						text = stringResource(
							id = R.string.budget_for,
							AppUtil.longMonths[Calendar.getInstance()[Calendar.MONTH]]
						),
						style = Typography.titleSmall.copy(
							color = if (uiMode.isDarkTheme()) black09 else black02,
							fontWeight = FontWeight.SemiBold,
							fontSize = Typography.titleSmall.fontSize.spScaled
						),
						modifier = Modifier
							.weight(0.46f)
					)
					
					Text(
						textAlign = TextAlign.End,
						text = CurrencyFormatter.format(
							locale = deviceLocale,
							amount = remainingBudget,
							currencyCode = LocalCurrency.current.countryCode
						),
						style = Typography.titleMedium.copy(
							color = if (uiMode.isDarkTheme()) black09 else black02,
							fontWeight = FontWeight.Bold,
							fontSize = Typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.weight(0.54f)
							.horizontalScroll(
								state = rememberScrollState(),
								autoRestart = true
							)
					)
				}
			}
			
			BudgetProgressIndicator(
				indicatorTrackColor = if (uiMode.isLightTheme()) Color.Black.copy(alpha = 0.08f)
						else Color.White.copy(alpha = 0.36f),
				stepColor = MaterialTheme.colorScheme.tertiaryContainer,
				progress = if (remainingBudget < 0) 1f else indicatorProgress,
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dpScaled)
					.clip(full_shape)
			)
		}
	}
}
