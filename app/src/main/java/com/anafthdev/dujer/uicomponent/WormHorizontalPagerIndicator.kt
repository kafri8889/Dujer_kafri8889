package com.anafthdev.dujer.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlin.math.abs

@ExperimentalPagerApi
@Composable
fun WormHorizontalPagerIndicator(
	pagerState: PagerState,
	modifier: Modifier = Modifier,
	activeColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
	inactiveColor: Color = activeColor.copy(ContentAlpha.disabled),
	indicatorWidth: Dp = 8.dp,
	activeIndicatorWidth: Dp = 24.dp,
	indicatorHeight: Dp = indicatorWidth,
	spacing: Dp = indicatorWidth,
	indicatorShape: Shape = CircleShape,
) {
	Box(
		modifier = modifier,
		contentAlignment = Alignment.CenterStart
	) {
		FlowRow(
			mainAxisAlignment = FlowMainAxisAlignment.Center,
			crossAxisAlignment = FlowCrossAxisAlignment.Center,
			mainAxisSpacing = spacing,
			crossAxisSpacing = spacing
		) {
			repeat(pagerState.pageCount) { index ->
				val currentPageOffset = pagerState.currentPageOffset
				val isCurrentPage = index == pagerState.currentPage
				val isNextPage = index == pagerState.targetPage
				
				val width: Dp = when {
					isCurrentPage -> {
						(activeIndicatorWidth * (1 - abs(currentPageOffset))).coerceIn(
							indicatorWidth,
							activeIndicatorWidth
						)
					}
					isNextPage -> {
						(activeIndicatorWidth * abs(currentPageOffset)).coerceIn(
							indicatorWidth,
							activeIndicatorWidth
						)
					}
					else -> {
						indicatorWidth
					}
				}
				val color: Color = when {
					isCurrentPage -> {
						lerp(
							activeColor,
							inactiveColor,
							abs(currentPageOffset).coerceIn(0f, 1f)
						)
					}
					isNextPage -> {
						lerp(
							inactiveColor,
							activeColor,
							abs(currentPageOffset).coerceIn(0f, 1f)
						)
					}
					else -> {
						inactiveColor
					}
				}
				
				Box(
					Modifier
						.size(
							width = width,
							height = indicatorHeight
						)
						.background(
							color = color,
							shape = indicatorShape
						)
				)
			}
		}
	}
}