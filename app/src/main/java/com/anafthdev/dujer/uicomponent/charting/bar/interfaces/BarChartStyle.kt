package com.anafthdev.dujer.uicomponent.charting.bar.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.anafthdev.dujer.uicomponent.charting.bar.components.BarAlignment
import com.anafthdev.dujer.uicomponent.charting.bar.components.XAxisVisibility
import com.anafthdev.dujer.uicomponent.charting.bar.components.YAxisPosition

interface BarChartStyle {
	
	@Composable
	fun showXAxis(): State<Boolean>
	
	@Composable
	fun showYAxis(): State<Boolean>
	
	@Composable
	fun xAxisVisibility(): State<XAxisVisibility>
	
	@Composable
	fun yAxisPosition(): State<YAxisPosition>
	
	@Composable
	fun barAlignment(): State<BarAlignment>
	
}