package com.anafthdev.dujer.uicomponent.charting.tokens.extension

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.anafthdev.dujer.uicomponent.charting.tokens.ChartingShapeTokens

@Composable
fun ChartingShapeTokens.toShape(): CornerBasedShape {
	return when (this) {
		ChartingShapeTokens.ExtraSmall -> MaterialTheme.shapes.extraSmall
		ChartingShapeTokens.Small -> MaterialTheme.shapes.small
		ChartingShapeTokens.Medium -> MaterialTheme.shapes.medium
		ChartingShapeTokens.Large -> MaterialTheme.shapes.large
		ChartingShapeTokens.ExtraLarge -> MaterialTheme.shapes.extraLarge
	}
}
