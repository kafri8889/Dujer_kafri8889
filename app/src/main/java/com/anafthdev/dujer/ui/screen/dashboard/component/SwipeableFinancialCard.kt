package com.anafthdev.dujer.ui.screen.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.ui.component.FinancialCard
import com.anafthdev.dujer.ui.screen.financial.FinancialViewModel
import com.anafthdev.dujer.ui.theme.big_shape

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableFinancialCard(
	financial: Financial,
	onDismissToEnd: () -> Unit,
	onClick: () -> Unit,
	onCanDelete: () -> Unit
) {
	
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
		((2 * dismissState.progress.fraction) >= 1f) and
		(dismissState.targetValue == DismissValue.DismissedToEnd) and
		!canDelete
	) {
		onCanDelete()
		canDelete = true
	}
	
	SwipeToDismiss(
		state = dismissState,
		directions = setOf(DismissDirection.StartToEnd),
		dismissThresholds = { FractionalThreshold(.6f) },
		background = {
			Box(
				modifier = Modifier
					.padding(
						horizontal = 14.dpScaled,
						vertical = 8.dpScaled
					)
					.fillMaxSize()
					.clip(big_shape)
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
		}
	) {
		FinancialCard(
			financial = financial,
			onClick = onClick,
			modifier = Modifier
				.padding(8.dpScaled)
				.fillMaxWidth()
		)
	}
}
