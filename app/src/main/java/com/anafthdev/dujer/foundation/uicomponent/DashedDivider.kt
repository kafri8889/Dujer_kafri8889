package com.anafthdev.dujer.foundation.uicomponent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun DashedDividerPreview() {
	DashedDivider(
		color = Color.Black,
		thickness = 1.dp,
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	)
}

@Composable
fun DashedDivider(
	thickness: Dp,
	color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
	phase: Float = 10f,
	intervals: FloatArray = floatArrayOf(10f, 10f),
	modifier: Modifier
) {
	Canvas(
		modifier = modifier
	) {
		val dividerHeight = thickness.toPx()
		drawRoundRect(
			color = color,
			style = Stroke(
				width = dividerHeight,
				pathEffect = PathEffect.dashPathEffect(
					intervals = intervals,
					phase = phase
				)
			)
		)
	}
}
