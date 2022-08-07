package com.anafthdev.dujer.foundation.uicomponent.charting.bar.interfaces

import androidx.compose.animation.core.AnimationSpec
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
	fun startPaddingBarContainer(selected: Boolean): State<Dp>
	
	@Composable
	fun endPaddingBarContainer(selected: Boolean): State<Dp>
	
	@Composable
	fun showXAxisLine(selected: Boolean): State<Boolean>
	
	@Composable
	fun xAxisLineColor(selected: Boolean): State<Color>
	
	@Composable
	fun xAxisTextStyle(selected: Boolean): State<TextStyle>
	
	@Composable
	fun xAxisLineAnimationSpec(selected: Boolean): State<AnimationSpec<Float>>
	
	@Composable
	fun showYAxisLine(selected: Boolean): State<Boolean>
	
	@Composable
	fun yAxisLineColor(selected: Boolean): State<Color>
	
	@Composable
	fun yAxisTextStyle(selected: Boolean): State<TextStyle>
	
}