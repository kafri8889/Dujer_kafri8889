package com.anafthdev.dujer.ui.category.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.ui.theme.large_shape
import com.anafthdev.dujer.ui.theme.swipe_dismiss_delete_background
import com.anafthdev.dujer.ui.theme.swipe_dismiss_edit_background

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableCategory(
	category: Category,
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
	onDismissToEnd: () -> Unit,
	onDismissToStart: () -> Unit
) {
	// TODO: Swipe kiri buat edit
	val dismissState = rememberDismissState(
		confirmStateChange = {
			when (it) {
				DismissValue.DismissedToStart -> onDismissToStart()
				DismissValue.DismissedToEnd -> onDismissToEnd()
				else -> {}
			}
			
			true
		}
	)
	
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
					DismissDirection.StartToEnd -> swipe_dismiss_delete_background
					DismissDirection.EndToStart -> swipe_dismiss_edit_background
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
			category = category,
			onClick = onClick
		)
	}
}
