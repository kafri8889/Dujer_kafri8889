package com.anafthdev.dujer.uicomponent.charting.bar.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

interface BarColors {
	
	@Composable
	fun barColor(selected: Boolean): State<Color>

}