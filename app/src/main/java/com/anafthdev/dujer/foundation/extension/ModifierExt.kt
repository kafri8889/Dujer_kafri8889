package com.anafthdev.dujer.foundation.extension

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.util.AppUtil.toast

fun Modifier.sizeBasedWidth(
	enlargement: Float = 1f
): Modifier = composed {
	val density = LocalDensity.current
	var width by remember { mutableStateOf(0.dp) }
	
	onGloballyPositioned {
		width = with(density) {
			it.size.width
				.times(enlargement)
				.toDp()
		}
	}.size(width)
}
