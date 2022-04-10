package com.anafthdev.dujer.ui.screen.dashboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.normal_shape

@Composable
fun FABNewFinancial(
	modifier: Modifier = Modifier,
	onNewIncome: () -> Unit,
	onNewExpense: () -> Unit,
) {
	
	val expandFabDelayMillis = 350
	val expandFabDurationMillis = 150
	val slideFabDurationMillis = 350
	
	var selectedFinancialType by remember { mutableStateOf(FinancialType.NOTHING) }
	var isFabExpanded by remember { mutableStateOf(false) }
	var incomeFabExpanded by remember { mutableStateOf(false) }
	var expenseFabExpanded by remember { mutableStateOf(false) }
	val fabIconRotationDegree by animateFloatAsState(
		targetValue = if (isFabExpanded) 225f else 0f,
		animationSpec = tween(500)
	)
	val primaryFabSize by animateDpAsState(
		targetValue = if (isFabExpanded) 40.dpScaled else 56.dpScaled,
		animationSpec = spring(
			dampingRatio = Spring.DampingRatioMediumBouncy
		),
		finishedListener = {
			if (!isFabExpanded) {
				when (selectedFinancialType) {
					FinancialType.INCOME -> onNewIncome()
					FinancialType.EXPENSE -> onNewExpense()
					else -> {}
				}
			}
		}
	)
	val incomeFabWidth by animateDpAsState(
		targetValue = if (isFabExpanded) 144.dpScaled else 56.dpScaled,
		animationSpec = tween(
			durationMillis = expandFabDurationMillis,
			delayMillis = if (isFabExpanded) expandFabDelayMillis else 0
		),
		finishedListener = {
			incomeFabExpanded = isFabExpanded
		}
	)
	val expenseFabWidth by animateDpAsState(
		targetValue = if (isFabExpanded) 144.dpScaled else 56.dpScaled,
		animationSpec = tween(
			durationMillis = expandFabDurationMillis,
			delayMillis = if (isFabExpanded) expandFabDelayMillis else 0
		),
		finishedListener = {
			expenseFabExpanded = isFabExpanded
		}
	)
	
	Column(
		modifier = modifier
	) {
		AnimatedVisibility(
			visible = isFabExpanded,
			enter = slideInVertically(
				animationSpec = tween(slideFabDurationMillis),
				initialOffsetY = { fullHeight -> 2 * fullHeight }
			),
			exit = slideOutVertically(
				targetOffsetY = { fullHeight -> 2 * fullHeight },
				animationSpec = tween(
					durationMillis = slideFabDurationMillis,
					delayMillis = expandFabDelayMillis
				)
			)
		) {
			FloatingActionButton(
				onClick = {
					selectedFinancialType = FinancialType.INCOME
					isFabExpanded = !isFabExpanded
				},
				modifier = Modifier
					.padding(bottom = 8.dpScaled)
					.width(incomeFabWidth)
			) {
				if (incomeFabExpanded and isFabExpanded) {
					Text(
						text = stringResource(id = R.string.income),
						style = Typography.bodyMedium.copy(
							fontSize = Typography.bodyMedium.fontSize.spScaled
						)
					)
				}
			}
		}
		
		AnimatedVisibility(
			visible = isFabExpanded,
			enter = slideInVertically(
				animationSpec = tween(slideFabDurationMillis),
				initialOffsetY = { fullHeight -> fullHeight }
			),
			exit = slideOutVertically(
				targetOffsetY = { fullHeight -> fullHeight },
				animationSpec = tween(
					durationMillis = slideFabDurationMillis,
					delayMillis = expandFabDelayMillis
				)
			)
		) {
			FloatingActionButton(
				onClick = {
					selectedFinancialType = FinancialType.EXPENSE
					isFabExpanded = !isFabExpanded
				},
				modifier = Modifier
					.padding(bottom = 8.dpScaled)
					.width(expenseFabWidth)
			) {
				if (expenseFabExpanded and isFabExpanded) {
					Text(
						text = stringResource(id = R.string.expenses),
						style = Typography.bodyMedium.copy(
							fontSize = Typography.bodyMedium.fontSize.spScaled
						)
					)
				}
			}
		}
		
		FloatingActionButton(
			onClick = {
				selectedFinancialType = FinancialType.NOTHING
				isFabExpanded = !isFabExpanded
			},
			modifier = Modifier
				.size(primaryFabSize)
				.align(Alignment.End)
		) {
			Icon(
				imageVector = Icons.Rounded.Add,
				contentDescription = null,
				modifier = Modifier
					.rotate(fabIconRotationDegree)
			)
		}
	}
}
