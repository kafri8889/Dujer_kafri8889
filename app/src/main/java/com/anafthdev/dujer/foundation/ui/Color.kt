package com.anafthdev.dujer.foundation.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.anafthdev.dujer.ui.theme.*

data class UiColor(
	val headlineText: Color,
	val titleText: Color,
	val normalText: Color,
	val bodyText: Color,
	val labelText: Color
)

val LocalUiColor = compositionLocalOf {
	UiColor(
		headlineText = black02,
		titleText = black03,
		normalText = black01,
		bodyText = black05,
		labelText = black06
	)
}
