package com.anafthdev.dujer.ui.category.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.large_shape
import com.anafthdev.dujer.ui.theme.medium_shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
	category: Category,
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
			.clip(large_shape)
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
					.size(48.dpScaled)
					.clip(medium_shape)
					.background(category.tint.backgroundTint.toColor())
			) {
				Icon(
					painter = painterResource(id = category.iconID),
					tint = category.tint.iconTint.toColor(),
					contentDescription = null,
					modifier = Modifier
						.size(20.dpScaled)
				)
			}
			
			Text(
				text = category.name,
				overflow = TextOverflow.Ellipsis,
				style = Typography.bodyMedium.copy(
					color = LocalUiColor.current.titleText,
					fontSize = Typography.bodyMedium.fontSize.spScaled
				),
				modifier = Modifier
					.padding(8.dpScaled)
					.fillMaxWidth()
			)
		}
	}
}
