package com.anafthdev.dujer.uicomponent.charting.bar.interfaces

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

interface BarStyle {
	
	@Composable
	fun barColor(selected: Boolean): State<Color>
	
	@Composable
	fun barWidth(selected: Boolean): State<Dp>
	
	@Composable
	fun barShape(selected: Boolean): State<CornerBasedShape>
	
	@Composable
	fun barContainerWidth(selected: Boolean): State<Dp>
	
	@Composable
	fun horizontalBarContainerPadding(selected: Boolean): State<Dp>
	
	@Composable
	fun xAxisTextStyle(selected: Boolean): State<TextStyle>
	
	@Composable
	fun yAxisTextStyle(selected: Boolean): State<TextStyle>
	
}