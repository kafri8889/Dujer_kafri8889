package com.anafthdev.dujer.foundation.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.anafthdev.dujer.ui.theme.black02
import com.anafthdev.dujer.ui.theme.black03
import com.anafthdev.dujer.ui.theme.black05
import com.anafthdev.dujer.ui.theme.black06

data class UiColor(
	val headlineText: Color,
	val titleText: Color,
	val bodyText: Color,
	val labelText: Color
)

val LocalUiColor = compositionLocalOf {
	UiColor(
		headlineText = black02,
		titleText = black03,
		bodyText = black05,
		labelText = black06
	)
}
