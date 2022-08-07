package com.anafthdev.dujer.foundation.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.theme.black01
import com.anafthdev.dujer.feature.theme.large_shape
import com.anafthdev.dujer.feature.theme.swipe_dismiss_delete_background
import com.anafthdev.dujer.foundation.window.dpScaled

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableFinancialCard(
	financial: Financial,
	modifier: Modifier = Modifier,
	onDismissToEnd: () -> Unit,
	onClick: () -> Unit
) {
	
	val dismissState = rememberDismissState(
		confirmStateChange = {
			if (it == DismissValue.DismissedToEnd) {
				onDismissToEnd()
			}
			
			true
		}
	)
	
	SwipeToDismiss(
		state = dismissState,
		directions = setOf(DismissDirection.StartToEnd),
		dismissThresholds = { FractionalThreshold(.6f) },
		background = {
			Box(
				modifier = Modifier
					.padding(
						horizontal = 6.dpScaled,
						vertical = 8.dpScaled
					)
					.fillMaxSize()
					.clip(large_shape)
					.background(swipe_dismiss_delete_background)
					.align(Alignment.CenterVertically)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_trash),
					tint = black01,
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
		FinancialCard(
			financial = financial,
			onClick = onClick,
			modifier = Modifier
				.padding(
					vertical = 8.dpScaled
				)
				.fillMaxWidth()
		)
	}
}
