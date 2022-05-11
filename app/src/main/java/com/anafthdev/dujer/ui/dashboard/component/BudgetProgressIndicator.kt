package com.anafthdev.dujer.ui.dashboard.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.LayoutDirection
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.ui.theme.balance_card_background
import com.anafthdev.dujer.ui.theme.dangerColor
import com.anafthdev.dujer.ui.theme.saveColor
import com.anafthdev.dujer.ui.theme.warningColor

@Composable
fun BudgetProgressIndicator(
	progress: Float,
	modifier: Modifier = Modifier
) {
	
	val color = when {
		progress <= 0.3f -> saveColor
		progress <= 0.8f -> warningColor
		progress <= 1f -> dangerColor
		else -> Color.Transparent
	}
	
	Canvas(
		modifier
			.progressSemantics(progress)
			.size(LinearIndicatorWidth, LinearIndicatorHeight)
	) {
		val strokeWidth = size.height
		drawLinearIndicatorTrack(Color.White.copy(alpha = 0.36f), strokeWidth)
		drawLinearIndicator(
			0f,
			progress,
			color,
			strokeWidth
		)
	}
}

private fun DrawScope.drawLinearIndicator(
	startFraction: Float,
	endFraction: Float,
	color: Color,
	strokeWidth: Float
) {
	val width = size.width
	val height = size.height
	// Start drawing from the vertical center of the stroke
	val yOffset = height / 2
	
	val isLtr = layoutDirection == LayoutDirection.Ltr
	val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
	val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width
	
	// Progress line
	drawLine(color, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth)
	
	drawLine(
		color = balance_card_background,
		start = Offset(
			x = (if (isLtr) 0.3f else 1f - 0.31f) * width,
			y = yOffset
		),
		end = Offset(
			(if (isLtr) 0.31f else 1f - 0.3f) * width,
			yOffset
		),
		strokeWidth = strokeWidth
	)
	
	drawLine(
		color = balance_card_background,
		start = Offset(
			x = (if (isLtr) 0.8f else 1f - 0.81f) * width,
			y = yOffset
		),
		end = Offset(
			(if (isLtr) 0.81f else 1f - 0.8f) * width,
			yOffset
		),
		strokeWidth = strokeWidth
	)
}

private fun DrawScope.drawLinearIndicatorTrack(
	color: Color,
	strokeWidth: Float
) = drawLinearIndicator(0f, 1f, color, strokeWidth)

internal val LinearIndicatorWidth = 240.dpScaled
internal val LinearIndicatorHeight = 4.dpScaled
