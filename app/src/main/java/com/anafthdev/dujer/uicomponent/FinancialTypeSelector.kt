package com.anafthdev.dujer.uicomponent

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.expense_card_background
import com.anafthdev.dujer.ui.theme.income_card_background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialTypeSelector(
	selectedFinancialType: FinancialType,
	modifier: Modifier = Modifier,
	onFinancialTypeChanged: (FinancialType) -> Unit
) {
	Row(
		modifier = modifier
	) {
		FilterChip(
			selected = selectedFinancialType == FinancialType.INCOME,
			colors = FilterChipDefaults.filterChipColors(
				selectedContainerColor = income_card_background
			),
			label = {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center,
					modifier = Modifier
						.fillMaxSize()
				) {
					Text(
						text = stringResource(id = R.string.income),
						style = MaterialTheme.typography.bodyMedium.copy(
							textAlign = TextAlign.Center,
							fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
						)
					)
				}
			},
			onClick = {
				onFinancialTypeChanged(FinancialType.INCOME)
			},
			modifier = Modifier
				.padding(
					start = 4.dpScaled,
					end = 2.dpScaled
				)
				.height(36.dpScaled)
				.weight(1f)
		)
		
		FilterChip(
			selected = selectedFinancialType == FinancialType.EXPENSE,
			colors = FilterChipDefaults.filterChipColors(
				selectedContainerColor = expense_card_background
			),
			label = {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center,
					modifier = Modifier
						.fillMaxSize()
				) {
					Text(
						text = stringResource(id = R.string.expenses),
						style = MaterialTheme.typography.bodyMedium.copy(
							textAlign = TextAlign.Center,
							fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
						)
					)
				}
			},
			onClick = {
				onFinancialTypeChanged(FinancialType.EXPENSE)
			},
			modifier = Modifier
				.padding(
					start = 2.dpScaled,
					end = 4.dpScaled
				)
				.height(36.dpScaled)
				.weight(1f)
		)
	}
}
