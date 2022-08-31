package com.anafthdev.dujer.foundation.uicomponent.charting.bar.data

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.foundation.uicomponent.charting.tokens.ChartingColorSchemeKeyTokens
import com.anafthdev.dujer.foundation.uicomponent.charting.tokens.ChartingShapeTokens
import com.anafthdev.dujer.foundation.uicomponent.charting.tokens.ChartingTypographyTokens

object BarTokens {
	
	const val UnSelectedBarColorAlpha = 0.12f
	
	const val SelectedShowXAxisLine = true
	const val UnSelectedShowXAxisLine = true
	
	const val SelectedShowYAxisLine = true
	const val UnSelectedShowYAxisLine = true
	
	val SelectedXAxisLineColor = ChartingColorSchemeKeyTokens.Outline
	val UnSelectedXAxisLineColor = ChartingColorSchemeKeyTokens.Outline
	
	val SelectedYAxisLineColor = ChartingColorSchemeKeyTokens.Outline
	val UnSelectedYAxisLineColor = ChartingColorSchemeKeyTokens.Outline
	
	val SelectedBarColor = ChartingColorSchemeKeyTokens.Primary
	
	val UnSelectedBarColor = ChartingColorSchemeKeyTokens.OnSurface
	
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
	
	val selectedXAxisLineAnimationSpec: AnimationSpec<Float> = spring(
		stiffness = Spring.StiffnessLow
	)
	val unSelectedXAxisLineAnimationSpec: AnimationSpec<Float> = spring(
		stiffness = Spring.StiffnessLow
	)

}