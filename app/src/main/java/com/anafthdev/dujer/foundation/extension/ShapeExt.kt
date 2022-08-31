package com.anafthdev.dujer.foundation.extension

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun ShapeKeyTokens.toShape(): Shape {
	return MaterialTheme.shapes.fromToken(this)
}

fun Shapes.fromToken(value: ShapeKeyTokens): Shape {
	return when (value) {
		ShapeKeyTokens.CornerExtraLarge -> extraLarge
		ShapeKeyTokens.CornerExtraLargeTop -> extraLarge.top()
		ShapeKeyTokens.CornerExtraSmall -> extraSmall
		ShapeKeyTokens.CornerExtraSmallTop -> extraSmall.top()
		ShapeKeyTokens.CornerFull -> CircleShape
		ShapeKeyTokens.CornerLarge -> large
		ShapeKeyTokens.CornerLargeEnd -> large.end()
		ShapeKeyTokens.CornerLargeTop -> large.top()
		ShapeKeyTokens.CornerMedium -> medium
		ShapeKeyTokens.CornerNone -> RoundedCornerShape(0)
		ShapeKeyTokens.CornerSmall -> small
	}
}

fun CornerBasedShape.end(): CornerBasedShape {
	return copy(topStart = CornerSize(0.0.dp), bottomStart = CornerSize(0.0.dp))
}

fun CornerBasedShape.top(): CornerBasedShape {
	return copy(bottomStart = CornerSize(0.0.dp), bottomEnd = CornerSize(0.0.dp))
}

enum class ShapeKeyTokens {
	CornerExtraLarge,
	CornerExtraLargeTop,
	CornerExtraSmall,
	CornerExtraSmallTop,
	CornerFull,
	CornerLarge,
	CornerLargeEnd,
	CornerLargeTop,
	CornerMedium,
	CornerNone,
	CornerSmall,
}
