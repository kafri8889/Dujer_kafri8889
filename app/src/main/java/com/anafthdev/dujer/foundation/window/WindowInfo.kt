package com.anafthdev.dujer.foundation.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val MinLargeScreenWidthDp = 585

var isLargeScreen = false

val LocalWindowInfo = compositionLocalOf { WindowInfo.default }

val Int.dpScaled: Dp get() = if (isLargeScreen) (this + (this / 2)).dp else this.dp

val Int.spScaled: TextUnit get() = if (isLargeScreen) (this + (this / 2)).sp else this.sp

val Double.dpScaled: Dp get() = if (isLargeScreen) (this + (this / 2)).dp else this.dp

val Float.dpScaled: Dp get() = if (isLargeScreen) (this + (this / 2)).dp else this.dp

val TextUnit.spScaled: TextUnit get() = if (isLargeScreen) (this.value + (this.value / 2)).sp else this

@Composable
fun rememberWindowInfo(): WindowInfo {
	val config = LocalConfiguration.current
	
	isLargeScreen = config.smallestScreenWidthDp > MinLargeScreenWidthDp
	
	return WindowInfo(
		screenWidthSize = getWindowSize(
			dimension = Dimension.WIDTH,
			sizeDp = config.screenWidthDp
		),
		screenHeightSize = getWindowSize(
			dimension = Dimension.HEIGHT,
			sizeDp = config.screenHeightDp
		),
		screenWidth = config.screenWidthDp.dp,
		screenHeight = config.screenHeightDp.dp,
		isLargeScreen = isLargeScreen
	)
}

private fun getWindowSize(dimension: Dimension, sizeDp: Int): WindowSize {
	return when (dimension) {
		Dimension.WIDTH -> when {
			sizeDp < WindowInfo.WIDTH_COMPACT -> WindowSize.COMPACT
			sizeDp < WindowInfo.WIDTH_MEDIUM -> WindowSize.MEDIUM
			else -> WindowSize.EXPANDED
		}
		Dimension.HEIGHT -> when {
			sizeDp < WindowInfo.HEIGHT_COMPACT -> WindowSize.COMPACT
			sizeDp < WindowInfo.HEIGHT_MEDIUM -> WindowSize.MEDIUM
			else -> WindowSize.EXPANDED
		}
	}
}

data class WindowInfo(
	val screenWidthSize: WindowSize,
	val screenHeightSize: WindowSize,
	val screenWidth: Dp,
	val screenHeight: Dp,
	val isLargeScreen: Boolean
) {
	companion object {
		
		const val WIDTH_COMPACT = 600
		const val WIDTH_MEDIUM = 840
		
		const val HEIGHT_COMPACT = 480
		const val HEIGHT_MEDIUM = 900
		
		val default = WindowInfo(
			screenWidthSize = WindowSize.COMPACT,
			screenHeightSize = WindowSize.COMPACT,
			screenWidth = WIDTH_COMPACT.dp,
			screenHeight = HEIGHT_COMPACT.dp,
			isLargeScreen = false
		)
		
	}
}
