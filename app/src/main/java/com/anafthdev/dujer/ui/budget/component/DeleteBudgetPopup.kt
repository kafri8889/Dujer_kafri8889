package com.anafthdev.dujer.ui.budget.component

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
import androidx.compose.ui.text.font.FontWeight
import com.anafthdev.dujer.R
import com.anafthdev.dujer.foundation.extension.isLightTheme
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.danger_color
import com.anafthdev.dujer.ui.theme.medium_shape

@Composable
fun DeleteBudgetPopup(
	onCancel: () -> Unit,
	onDelete: () -> Unit,
	onClickOutside: () -> Unit
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
				onClick = {
					onClickOutside()
				}
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
				text = stringResource(id = R.string.delete_budget),
				style = MaterialTheme.typography.bodyLarge.copy(
					fontWeight = FontWeight.SemiBold,
					fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
				),
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
			)
			
			Text(
				text = stringResource(id = R.string.delete_budget_confirmation),
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
					onClick = onCancel
				) {
					Text(
						text = stringResource(id = R.string.cancel)
					)
				}
				
				ElevatedButton(
					shape = MaterialTheme.shapes.medium,
					colors = ButtonDefaults.elevatedButtonColors(
						containerColor = danger_color
					),
					onClick = onDelete,
					modifier = Modifier
						.padding(
							horizontal = 8.dpScaled
						)
				) {
					Text(
						text = stringResource(id = R.string.delete),
						style = LocalTextStyle.current.copy(
							color = Color.White
						)
					)
				}
			}
		}
	}
}
