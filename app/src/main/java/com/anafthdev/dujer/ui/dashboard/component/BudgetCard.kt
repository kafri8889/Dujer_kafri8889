package com.anafthdev.dujer.ui.dashboard.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.R
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.balance_card_background
import com.anafthdev.dujer.ui.theme.full_shape
import com.anafthdev.dujer.ui.theme.large_shape
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetCard(
	totalExpense: Double,
	totalIncome: Double
) {
	
	val indicatorProgress = try {
		(totalExpense / totalIncome).toFloat()
	} catch (e: Exception) { 0f }
	
	Card(
		shape = large_shape,
		elevation = CardDefaults.cardElevation(
			defaultElevation = 4.dp
		),
		colors = CardDefaults.cardColors(
			containerColor = balance_card_background
		),
		onClick = {},
		modifier = Modifier
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier
				.padding(16.dpScaled)
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
					style = Typography.bodyMedium.copy(
						color = Color.White,
						fontWeight = FontWeight.SemiBold,
						fontSize = Typography.bodyMedium.fontSize.spScaled
					)
				)
				
				Spacer(
					modifier = Modifier
						.weight(1f)
				)
				
				Text(
					text = CurrencyFormatter.format(
						locale = AppUtil.deviceLocale,
						amount = totalIncome - totalExpense,
						currencyCode = LocalCurrency.current.countryCode
					),
					style = Typography.titleMedium.copy(
						color = Color.White,
						fontWeight = FontWeight.Bold,
						fontSize = Typography.titleMedium.fontSize.spScaled
					),
					modifier = Modifier
						.padding(start = 16.dpScaled)
						.horizontalScroll(
							state = rememberScrollState(),
							autoRestart = true
						)
				)
			}
			
			BudgetProgressIndicator(
				progress = indicatorProgress,
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dpScaled)
					.clip(full_shape)
			)
		}
	}
}
