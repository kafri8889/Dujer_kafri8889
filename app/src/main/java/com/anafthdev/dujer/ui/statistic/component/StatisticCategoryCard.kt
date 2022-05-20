package com.anafthdev.dujer.ui.statistic.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.extension.isDefault
import com.anafthdev.dujer.foundation.extension.lightenColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticCategoryCard(
	color: Color,
	category: Category,
	modifier: Modifier = Modifier
) {
	
	Card(
		shape = MaterialTheme.shapes.medium,
		border = BorderStroke(
			width = 1.dpScaled,
			color = MaterialTheme.colorScheme.outline
		),
		modifier = modifier
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dpScaled)
		) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.size(64.dpScaled)
					.clip(MaterialTheme.shapes.medium)
					.background(color)
			) {
				Icon(
					painter = painterResource(
						id = if (!category.isDefault()) category.iconID else R.drawable.ic_other
					),
					tint = color.lightenColor(0.6f),
					contentDescription = null
				)
			}
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(start = 8.dpScaled)
			) {
				Text(
					text = category.name,
					maxLines = 1,
					style = Typography.bodyMedium.copy(
						fontWeight = FontWeight.SemiBold,
						fontSize = Typography.bodyMedium.fontSize.spScaled
					)
				)
				
				Spacer(modifier = Modifier.weight(1f))
				
				Text(
					text = category.type.name,
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
