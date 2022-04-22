package com.anafthdev.dujer.ui.category.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.ui.theme.large_shape
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableCategory(
	category: Category,
	modifier: Modifier = Modifier,
	onCanDelete: () -> Unit,
	onDismissToEnd: () -> Unit,
	onDismissToStart: () -> Unit
) {
	// TODO: Swipe kiri buat edit
	var canDelete by remember { mutableStateOf(false) }
	
	val dismissState = rememberDismissState(
		confirmStateChange = {
			when (it) {
				DismissValue.DismissedToStart -> onDismissToStart().also {
					canDelete = false
				}
				DismissValue.DismissedToEnd -> onDismissToEnd()
				else -> canDelete = false
			}
			
			true
		}
	)
	
	if (
		((2 * dismissState.progress.fraction) >= 1f) and
		((dismissState.targetValue == DismissValue.DismissedToEnd) or (dismissState.targetValue == DismissValue.DismissedToStart)) and
		!canDelete
	) {
		onCanDelete()
		canDelete = true
	}
	
	LaunchedEffect(dismissState.isDismissed(DismissDirection.EndToStart)) {
		dismissState.reset()
	}
	
	SwipeToDismiss(
		state = dismissState,
		directions = if (!category.defaultCategory) setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart) else setOf(),
		dismissThresholds = { FractionalThreshold(.6f) },
		background = {
			val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
			
			val color by animateColorAsState(
				targetValue = when (direction) {
					DismissDirection.StartToEnd -> Color(0xFFFFA1A1)
					DismissDirection.EndToStart -> Color(0xFFABFF9C)
					else -> Color.Transparent
				}
			)
			
			val componentAlignment = when (direction) {
				DismissDirection.EndToStart -> Alignment.CenterEnd
				DismissDirection.StartToEnd -> Alignment.CenterStart
			}
			
			Box(
				modifier = Modifier
					.fillMaxSize()
					.clip(large_shape)
					.background(color)
					.align(Alignment.CenterVertically)
			) {
				Icon(
					painter = painterResource(
						id = if (direction == DismissDirection.StartToEnd) R.drawable.ic_trash
						else R.drawable.ic_edit
					),
					contentDescription = null,
					modifier = Modifier
						.padding(
							horizontal = 24.dpScaled
						)
						.align(componentAlignment)
				)
			}
		},
		modifier = modifier
	) {
		CategoryCard(
			category = category
		)
	}
}
