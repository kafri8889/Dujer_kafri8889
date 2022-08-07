package com.anafthdev.dujer.feature.change_currency.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.anafthdev.dujer.feature.theme.Typography
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCurrencyItem(
	selected: Boolean,
	currency: Currency,
	onSelected: () -> Unit
) {
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.clickable {
				onSelected()
			}
	) {
		RadioButton(
			selected = selected,
			onClick = onSelected,
			modifier = Modifier
				.padding(8.dpScaled)
		)
		
		Text(
			text = currency.country,
			style = Typography.bodyMedium.copy(
				fontWeight = FontWeight.Normal,
				fontSize = Typography.bodyMedium.fontSize.spScaled
			),
			modifier = Modifier
				.padding(end = 16.dpScaled)
				.weight(1f)
		)
		
		Text(
			text = currency.symbol,
			style = Typography.bodyMedium.copy(
				color = Color.Gray,
				fontWeight = FontWeight.Normal,
				fontSize = Typography.bodyMedium.fontSize.spScaled
			),
			modifier = Modifier
				.padding(
					horizontal = 16.dpScaled
				)
		)
	}
}
