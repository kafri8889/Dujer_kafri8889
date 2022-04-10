package com.anafthdev.dujer.ui.screen.financial.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.big_shape
import com.anafthdev.dujer.ui.theme.normal_shape

@Composable
fun FinancialTypeList(
	types: List<FinancialType>,
	onItemClick: (FinancialType) -> Unit
) {
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.clip(big_shape)
			.background(MaterialTheme.colorScheme.secondaryContainer)
	) {
		types.forEach { type ->
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.clickable {
						onItemClick(type)
					}
					.padding(8.dpScaled)
			) {
				Text(
					text = type.name,
					overflow = TextOverflow.Ellipsis,
					style = Typography.bodyMedium.copy(
						fontSize = Typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.padding(8.dpScaled)
						.fillMaxWidth()
				)
			}
		}
	}
}
