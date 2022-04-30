package com.anafthdev.dujer.uicomponent.charting.bar.data

import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.uicomponent.charting.tokens.ChartingColorSchemeKeyTokens
import com.anafthdev.dujer.uicomponent.charting.tokens.ChartingShapeTokens
import com.anafthdev.dujer.uicomponent.charting.tokens.ChartingTypographyTokens

object BarChartTokens {
	
	val SelectedBarColor = ChartingColorSchemeKeyTokens.Primary
	
	val UnSelectedBarColor = ChartingColorSchemeKeyTokens.OnSurface
	
	const val UnSelectedBarColorAlpha = 0.12f
	
	val SelectedBarWidth = 36.dp
	val UnSelectedBarWidth = 36.dp
	
	val SelectedBarContainerWidth = 40.dp
	val UnSelectedBarContainerWidth = 40.dp
	
	val SelectedHorizontalBarContainerPadding = 4.dp
	val UnSelectedHorizontalBarContainerPadding = 4.dp
	
	val SelectedBarShape = ChartingShapeTokens.Small
	val UnSelectedBarShape = ChartingShapeTokens.Small
	
	val SelectedXAxisTextStyle = ChartingTypographyTokens.BodySmall
	val UnSelectedXAxisTextStyle = ChartingTypographyTokens.BodySmall
	val SelectedYAxisTextStyle = ChartingTypographyTokens.BodySmall
	val UnSelectedYAxisTextStyle = ChartingTypographyTokens.BodySmall

}