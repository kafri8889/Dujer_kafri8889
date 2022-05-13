package com.anafthdev.dujer.ui.dashboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.anafthdev.dujer.foundation.window.spScaled

@Composable
fun DashboardNavigationRail(
	visible: Boolean,
	selectedItem: Pair<Int, Int>,
	items: List<Pair<Int, Int>>,
	onItemSelected: (Pair<Int, Int>) -> Unit
) {
	
	AnimatedVisibility(
		visible = visible,
		enter = expandHorizontally(
			animationSpec = tween(400),
		),
		exit = shrinkHorizontally(
			animationSpec = tween(400),
		),
		modifier = Modifier
			.systemBarsPadding()
			.fillMaxHeight()
	) {
		NavigationRail(
			containerColor = MaterialTheme.colorScheme.background,
			modifier = Modifier
				.fillMaxHeight()
		) {
			items.forEach { pair ->
				NavigationRailItem(
					selected = pair.first == selectedItem.first,
					icon = {
						Icon(
							painter = painterResource(id = pair.second),
							contentDescription = null
						)
					},
					label = {
						Text(
							text = stringResource(id = pair.first),
							style = LocalTextStyle.current.copy(
								fontSize = LocalTextStyle.current.fontSize.spScaled
							)
						)
					},
					onClick = {
						onItemSelected(pair)
					}
				)
			}
		}
	}
}
