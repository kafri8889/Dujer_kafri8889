package com.anafthdev.dujer.uicomponent.charting.bar.data

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.uicomponent.charting.bar.interfaces.BarColors
import com.anafthdev.dujer.uicomponent.charting.bar.interfaces.BarStyle
import com.anafthdev.dujer.uicomponent.charting.tokens.extension.toColor
import com.anafthdev.dujer.uicomponent.charting.tokens.extension.toShape
import com.anafthdev.dujer.uicomponent.charting.tokens.extension.toTextStyle

object BarChartDefault {
	
	@Composable
	fun barColor(
		selectedBarColor: Color = BarChartTokens.SelectedBarColor.toColor(),
		unSelectedBarColor: Color = BarChartTokens.UnSelectedBarColor
			.toColor()
			.copy(alpha = BarChartTokens.UnSelectedBarColorAlpha)
	): BarColors {
		return DefaultBarColor(
			selectedBarColor = selectedBarColor,
			unSelectedBarColor = unSelectedBarColor
		)
	}
	
	@Composable
	fun barStyle(
		selectedBarWidth: Dp = BarChartTokens.SelectedBarWidth,
		unSelectedBarWidth: Dp = BarChartTokens.UnSelectedBarWidth,
		selectedBarContainerWidth: Dp = BarChartTokens.SelectedBarContainerWidth,
		unSelectedBarContainerWidth: Dp = BarChartTokens.UnSelectedBarContainerWidth,
		selectedHorizontalBarContainerPadding: Dp = BarChartTokens.SelectedHorizontalBarContainerPadding,
		unSelectedHorizontalBarContainerPadding: Dp = BarChartTokens.UnSelectedHorizontalBarContainerPadding,
		selectedBarShape: CornerBasedShape = BarChartTokens.SelectedBarShape.toShape(),
		unSelectedBarShape: CornerBasedShape = BarChartTokens.UnSelectedBarShape.toShape(),
		selectedXAxisTextStyle: TextStyle = BarChartTokens.SelectedXAxisTextStyle.toTextStyle().copy(
			fontWeight = FontWeight.Medium,
			fontSize = BarChartTokens.SelectedXAxisTextStyle.toTextStyle().fontSize.spScaled
		),
		unSelectedXAxisTextStyle: TextStyle = BarChartTokens.UnSelectedXAxisTextStyle.toTextStyle().copy(
			fontWeight = FontWeight.Medium,
			fontSize = BarChartTokens.SelectedXAxisTextStyle.toTextStyle().fontSize.spScaled
		),
		selectedYAxisTextStyle: TextStyle = BarChartTokens.SelectedYAxisTextStyle.toTextStyle().copy(
			color = MaterialTheme.colorScheme.background,
			fontWeight = FontWeight.Medium,
			fontSize = BarChartTokens.SelectedXAxisTextStyle.toTextStyle().fontSize.spScaled
		),
		unSelectedYAxisTextStyle: TextStyle = BarChartTokens.UnSelectedYAxisTextStyle.toTextStyle().copy(
			fontWeight = FontWeight.Medium,
			fontSize = BarChartTokens.SelectedXAxisTextStyle.toTextStyle().fontSize.spScaled
		)
	): BarStyle {
		return DefaultBarStyle(
			selectedBarWidth = selectedBarWidth,
			unSelectedBarWidth = unSelectedBarWidth,
			selectedBarContainerWidth = selectedBarContainerWidth,
			unSelectedBarContainerWidth = unSelectedBarContainerWidth,
			selectedHorizontalBarContainerPadding = selectedHorizontalBarContainerPadding,
			unSelectedHorizontalBarContainerPadding = unSelectedHorizontalBarContainerPadding,
			selectedBarShape = selectedBarShape,
			unSelectedBarShape = unSelectedBarShape,
			selectedXAxisTextStyle = selectedXAxisTextStyle,
			unSelectedXAxisTextStyle = unSelectedXAxisTextStyle,
			selectedYAxisTextStyle = selectedYAxisTextStyle,
			unSelectedYAxisTextStyle = unSelectedYAxisTextStyle
		)
	}
	
	@Immutable
	private class DefaultBarColor(
		private val selectedBarColor: Color,
		private val unSelectedBarColor: Color,
	): BarColors {
		
		@Composable
		override fun barColor(selected: Boolean): State<Color> {
			return rememberUpdatedState(
				newValue = if (selected) selectedBarColor else unSelectedBarColor
			)
		}
	}
	
	@Immutable
	private class DefaultBarStyle(
		private val selectedBarWidth: Dp,
		private val unSelectedBarWidth: Dp,
		private val selectedBarContainerWidth: Dp,
		private val unSelectedBarContainerWidth: Dp,
		private val selectedHorizontalBarContainerPadding: Dp,
		private val unSelectedHorizontalBarContainerPadding: Dp,
		private val selectedBarShape: CornerBasedShape,
		private val unSelectedBarShape: CornerBasedShape,
		private val selectedXAxisTextStyle: TextStyle,
		private val unSelectedXAxisTextStyle: TextStyle,
		private val selectedYAxisTextStyle: TextStyle,
		private val unSelectedYAxisTextStyle: TextStyle
	): BarStyle {
		
		@Composable
		override fun barWidth(selected: Boolean): State<Dp> {
			return rememberUpdatedState(
				newValue = if (selected) selectedBarWidth else unSelectedBarWidth
			)
		}
		
		@Composable
		override fun barShape(selected: Boolean): State<CornerBasedShape> {
			return rememberUpdatedState(
				newValue = if (selected) selectedBarShape else unSelectedBarShape
			)
		}
		
		@Composable
		override fun barContainerWidth(selected: Boolean): State<Dp> {
			return rememberUpdatedState(
				newValue = if (selected) selectedBarContainerWidth else unSelectedBarContainerWidth
			)
		}
		
		@Composable
		override fun horizontalBarContainerPadding(selected: Boolean): State<Dp> {
			return rememberUpdatedState(
				newValue = if (selected) selectedHorizontalBarContainerPadding else unSelectedHorizontalBarContainerPadding
			)
		}
		
		@Composable
		override fun xAxisTextStyle(selected: Boolean): State<TextStyle> {
			return rememberUpdatedState(
				newValue = if (selected) selectedXAxisTextStyle else unSelectedXAxisTextStyle
			)
		}
		
		@Composable
		override fun yAxisTextStyle(selected: Boolean): State<TextStyle> {
			return rememberUpdatedState(
				newValue = if (selected) selectedYAxisTextStyle else unSelectedYAxisTextStyle
			)
		}
	}
	
}
