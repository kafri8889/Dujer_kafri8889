package com.anafthdev.dujer.uicomponent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.LayoutDirection
import com.anafthdev.dujer.foundation.extension.darkenColor
import com.anafthdev.dujer.foundation.extension.isLightTheme
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.ui.theme.dangerColor
import com.anafthdev.dujer.ui.theme.saveColor
import com.anafthdev.dujer.ui.theme.warningColor

@Composable
fun BudgetProgressIndicator(
	progress: Float,
	stepColor: Color,
	modifier: Modifier = Modifier,
	indicatorTrackColor: Color = Color.White.copy(alpha = 0.36f)
) {
	
	val uiMode = LocalUiMode.current
	
	val color = when {
		progress <= 0.3f -> saveColor.darkenColor(
			if (uiMode.isLightTheme()) 0.14f else 0f
		)
		progress <= 0.8f -> warningColor.darkenColor(
			if (uiMode.isLightTheme()) 0.14f else 0f
		)
		progress <= 1f -> dangerColor.darkenColor(
			if (uiMode.isLightTheme()) 0.1f else 0f
		)
		else -> Color.Transparent
	}
	
	Canvas(
		modifier
			.progressSemantics(progress)
			.size(LinearIndicatorWidth, LinearIndicatorHeight)
	) {
		val strokeWidth = size.height
		drawLinearIndicatorTrack(indicatorTrackColor, stepColor, strokeWidth)
		drawLinearIndicator(
			0f,
			progress,
			color,
			stepColor,
			strokeWidth
		)
	}
}

private fun DrawScope.drawLinearIndicator(
	startFraction: Float,
	endFraction: Float,
	color: Color,
	stepColor: Color,
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
		color = stepColor,
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
		color = stepColor,
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
	stepColor: Color,
	strokeWidth: Float
) = drawLinearIndicator(0f, 1f, color, stepColor, strokeWidth)

internal val LinearIndicatorWidth = 240.dpScaled
internal val LinearIndicatorHeight = 4.dpScaled
