package com.anafthdev.dujer.ui.category.component

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
			if (it == DismissValue.DismissedToEnd) {
				onDismissToEnd()
			} else {
				canDelete = false
			}
			
			true
		}
	)
	
	if (
		((2 * dismissState.progress.fraction) >= 1f) and !canDelete
	) {
		onCanDelete()
		canDelete = true
	}
	
	SwipeToDismiss(
		state = dismissState,
		directions = if (!category.defaultCategory) setOf(DismissDirection.StartToEnd) else setOf(),
		dismissThresholds = { FractionalThreshold(.6f) },
		background = {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.clip(large_shape)
					.background(Color(0xFFF38B8B))
					.align(Alignment.CenterVertically)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_trash),
					contentDescription = null,
					modifier = Modifier
						.padding(
							horizontal = 24.dpScaled
						)
						.align(Alignment.CenterStart)
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
