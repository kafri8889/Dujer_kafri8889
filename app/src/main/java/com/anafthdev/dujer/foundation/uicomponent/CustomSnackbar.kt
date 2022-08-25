package com.anafthdev.dujer.foundation.uicomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.anafthdev.dujer.R
import com.anafthdev.dujer.feature.theme.black01
import com.anafthdev.dujer.feature.theme.black10
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled

@Composable
fun CustomSnackbar(
	snackbarData: SnackbarData,
	onCancel: () -> Unit
) {
	
	val uiMode = LocalUiMode.current
	
	ElevatedCard(
		shape = MaterialTheme.shapes.large,
		colors = CardDefaults.elevatedCardColors(
			containerColor = MaterialTheme.colorScheme.inverseSurface
		),
		elevation = CardDefaults.elevatedCardElevation(
			defaultElevation = 8.dpScaled
		)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.padding(
					vertical = 4.dpScaled,
					horizontal = 8.dpScaled
				)
		) {
			Text(
				maxLines = 2,
				overflow = TextOverflow.Ellipsis,
				text = snackbarData.visuals.message,
				style = MaterialTheme.typography.bodyMedium.copy(
					fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
				),
				modifier = Modifier
					.weight(1f)
			)
			
			TextButton(
				enabled = snackbarData.visuals.actionLabel != null,
				contentPadding = PaddingValues(8.dpScaled),
				colors = ButtonDefaults.textButtonColors(
					contentColor = MaterialTheme.colorScheme.primaryContainer
				),
				onClick = {
					onCancel()
					snackbarData.dismiss()
				}
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					if (snackbarData.visuals.actionLabel != null) {
						Icon(
							painter = painterResource(id = R.drawable.ic_undo),
							contentDescription = null,
							tint = if (uiMode.isDarkTheme()) black01 else black10,
							modifier = Modifier
								.padding(8.dpScaled)
						)
					}
					
					Text(
						text = snackbarData.visuals.actionLabel ?: "",
						style = LocalTextStyle.current.copy(
							fontWeight = FontWeight.Medium,
							fontSize = LocalTextStyle.current.fontSize.spScaled
						)
					)
				}
			}
			
			AnimatedVisibility(visible = snackbarData.visuals.withDismissAction) {
				IconButton(
					onClick = {
						snackbarData.dismiss()
					}
				) {
					Icon(
						imageVector = Icons.Rounded.Close,
						contentDescription = null
					)
				}
			}
		}
	}
}
