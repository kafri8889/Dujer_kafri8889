package com.anafthdev.dujer.uicomponent.charting.bar.data

import androidx.compose.animation.core.AnimationSpec
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
import com.anafthdev.dujer.uicomponent.charting.bar.components.BarAlignment
import com.anafthdev.dujer.uicomponent.charting.bar.components.XAxisVisibility
import com.anafthdev.dujer.uicomponent.charting.bar.components.YAxisPosition
import com.anafthdev.dujer.uicomponent.charting.bar.interfaces.BarChartStyle
import com.anafthdev.dujer.uicomponent.charting.bar.interfaces.BarStyle
import com.anafthdev.dujer.uicomponent.charting.tokens.extension.toColor
import com.anafthdev.dujer.uicomponent.charting.tokens.extension.toShape
import com.anafthdev.dujer.uicomponent.charting.tokens.extension.toTextStyle

object BarChartDefault {
	
	@Composable
	fun barChartStyle(
		showXAxis: Boolean = BarChartTokens.ShowXAxis,
		showYAxis: Boolean = BarChartTokens.ShowYAxis,
		xAxisVisibility: XAxisVisibility = BarChartTokens.XAxisVisibility_,
		yAxisPosition: YAxisPosition = BarChartTokens.YAxisPosition_,
		barAlignment: BarAlignment = BarChartTokens.BarAlignment_
	): BarChartStyle {
		return DefaultBarChartStyle(
			showXAxis = showXAxis,
			showYAxis = showYAxis,
			xAxisVisibility = xAxisVisibility,
			yAxisPosition = yAxisPosition,
			barAlignment = barAlignment
		)
	}
	
	@Composable
	fun barStyle(
		selectedBarColor: Color = BarTokens.SelectedBarColor.toColor(),
		unSelectedBarColor: Color = BarTokens.UnSelectedBarColor
			.toColor()
			.copy(alpha = BarTokens.UnSelectedBarColorAlpha),
		
		selectedBarWidth: Dp = BarTokens.SelectedBarWidth,
		unSelectedBarWidth: Dp = BarTokens.UnSelectedBarWidth,
		
		selectedBarContainerWidth: Dp = BarTokens.SelectedBarContainerWidth,
		unSelectedBarContainerWidth: Dp = BarTokens.UnSelectedBarContainerWidth,
		
		selectedStartPaddingBarContainer: Dp = BarTokens.SelectedHorizontalBarContainerPadding,
		unSelectedStartPaddingBarContainer: Dp = BarTokens.UnSelectedHorizontalBarContainerPadding,
		
		selectedEndPaddingBarContainer: Dp = BarTokens.SelectedHorizontalBarContainerPadding,
		unSelectedEndPaddingBarContainer: Dp = BarTokens.UnSelectedHorizontalBarContainerPadding,
		
		selectedShowXAxisLine: Boolean = BarTokens.SelectedShowXAxisLine,
		unSelectedShowXAxisLine: Boolean = BarTokens.UnSelectedShowXAxisLine,
		
		selectedXAxisLineColor: Color = BarTokens.SelectedXAxisLineColor.toColor(),
		unSelectedXAxisLineColor: Color = BarTokens.UnSelectedXAxisLineColor.toColor(),
		
		selectedYAxisLineColor: Color = BarTokens.SelectedYAxisLineColor.toColor(),
		unSelectedYAxisLineColor: Color = BarTokens.UnSelectedYAxisLineColor.toColor(),
		
		selectedShowYAxisLine: Boolean = BarTokens.SelectedShowYAxisLine,
		unSelectedShowYAxisLine: Boolean = BarTokens.UnSelectedShowYAxisLine,
		
		selectedBarShape: CornerBasedShape = BarTokens.SelectedBarShape.toShape(),
		unSelectedBarShape: CornerBasedShape = BarTokens.UnSelectedBarShape.toShape(),
		
		selectedXAxisTextStyle: TextStyle = BarTokens.SelectedXAxisTextStyle.toTextStyle().copy(
			fontWeight = FontWeight.Medium,
			fontSize = BarTokens.SelectedXAxisTextStyle.toTextStyle().fontSize.spScaled
		),
		unSelectedXAxisTextStyle: TextStyle = BarTokens.UnSelectedXAxisTextStyle.toTextStyle().copy(
			fontWeight = FontWeight.Medium,
			fontSize = BarTokens.SelectedXAxisTextStyle.toTextStyle().fontSize.spScaled
		),
		
		selectedXAxisLineAnimationSpec: AnimationSpec<Float> = BarTokens.selectedXAxisLineAnimationSpec,
		unSelectedXAxisLineAnimationSpec: AnimationSpec<Float> = BarTokens.unSelectedXAxisLineAnimationSpec,
		
		selectedYAxisTextStyle: TextStyle = BarTokens.SelectedYAxisTextStyle.toTextStyle().copy(
			color = MaterialTheme.colorScheme.background,
			fontWeight = FontWeight.Medium,
			fontSize = BarTokens.SelectedXAxisTextStyle.toTextStyle().fontSize.spScaled
		),
		unSelectedYAxisTextStyle: TextStyle = BarTokens.UnSelectedYAxisTextStyle.toTextStyle().copy(
			fontWeight = FontWeight.Medium,
			fontSize = BarTokens.SelectedXAxisTextStyle.toTextStyle().fontSize.spScaled
		)
	): BarStyle {
		return DefaultBarStyle(
			selectedBarColor = selectedBarColor,
			unSelectedBarColor = unSelectedBarColor,
			
			selectedBarWidth = selectedBarWidth,
			unSelectedBarWidth = unSelectedBarWidth,
			
			selectedBarContainerWidth = selectedBarContainerWidth,
			unSelectedBarContainerWidth = unSelectedBarContainerWidth,
			
			selectedStartPaddingBarContainer = selectedStartPaddingBarContainer,
			unSelectedStartPaddingBarContainer = unSelectedStartPaddingBarContainer,
			
			selectedEndPaddingBarContainer = selectedEndPaddingBarContainer,
			unSelectedEndPaddingBarContainer = unSelectedEndPaddingBarContainer,
			
			selectedShowXAxisLine = selectedShowXAxisLine,
		 	unSelectedShowXAxisLine = unSelectedShowXAxisLine,
		
		 	selectedXAxisLineColor = selectedXAxisLineColor,
		 	unSelectedXAxisLineColor = unSelectedXAxisLineColor,
		
		 	selectedShowYAxisLine = selectedShowYAxisLine,
		 	unSelectedShowYAxisLine = unSelectedShowYAxisLine,
		
			selectedYAxisLineColor = selectedYAxisLineColor,
		 	unSelectedYAxisLineColor = unSelectedYAxisLineColor,
			
			selectedBarShape = selectedBarShape,
			unSelectedBarShape = unSelectedBarShape,
			
			selectedXAxisTextStyle = selectedXAxisTextStyle,
			unSelectedXAxisTextStyle = unSelectedXAxisTextStyle,
			
			selectedXAxisLineAnimationSpec = selectedXAxisLineAnimationSpec,
			unSelectedXAxisLineAnimationSpec = unSelectedXAxisLineAnimationSpec,
			
			selectedYAxisTextStyle = selectedYAxisTextStyle,
			unSelectedYAxisTextStyle = unSelectedYAxisTextStyle
		)
	}
	
	@Immutable
	private class DefaultBarChartStyle(
		private val showXAxis: Boolean,
		private val showYAxis: Boolean,
		private val xAxisVisibility: XAxisVisibility,
		private val yAxisPosition: YAxisPosition,
		private val barAlignment: BarAlignment
	): BarChartStyle {
		
		@Composable
		override fun showXAxis(): State<Boolean> {
			return rememberUpdatedState(
				newValue = showXAxis
			)
		}
		
		@Composable
		override fun showYAxis(): State<Boolean> {
			return rememberUpdatedState(
				newValue = showYAxis
			)
		}
		
		@Composable
		override fun xAxisVisibility(): State<XAxisVisibility> {
			return rememberUpdatedState(
				newValue = xAxisVisibility
			)
		}
		
		@Composable
		override fun yAxisPosition(): State<YAxisPosition> {
			return rememberUpdatedState(
				newValue = yAxisPosition
			)
		}
		
		@Composable
		override fun barAlignment(): State<BarAlignment> {
			return rememberUpdatedState(
				newValue = barAlignment
			)
		}
	}
	
	@Immutable
	private class DefaultBarStyle(
		private val selectedBarColor: Color,
		private val unSelectedBarColor: Color,
		
		private val selectedBarWidth: Dp,
		private val unSelectedBarWidth: Dp,
		
		private val selectedBarContainerWidth: Dp,
		private val unSelectedBarContainerWidth: Dp,
		
		private val selectedStartPaddingBarContainer: Dp,
		private val unSelectedStartPaddingBarContainer: Dp,
		
		private val selectedEndPaddingBarContainer: Dp,
		private val unSelectedEndPaddingBarContainer: Dp,
		
		private val selectedShowXAxisLine: Boolean,
		private val unSelectedShowXAxisLine: Boolean,
		
		private val selectedXAxisLineColor: Color,
		private val unSelectedXAxisLineColor: Color,
		
		private val selectedXAxisLineAnimationSpec: AnimationSpec<Float>,
		private val unSelectedXAxisLineAnimationSpec: AnimationSpec<Float>,
		
		private val selectedShowYAxisLine: Boolean,
		private val unSelectedShowYAxisLine: Boolean,
		
		private val selectedYAxisLineColor: Color,
		private val unSelectedYAxisLineColor: Color,
		
		private val selectedBarShape: CornerBasedShape,
		private val unSelectedBarShape: CornerBasedShape,
		
		private val selectedXAxisTextStyle: TextStyle,
		private val unSelectedXAxisTextStyle: TextStyle,
		
		private val selectedYAxisTextStyle: TextStyle,
		private val unSelectedYAxisTextStyle: TextStyle
	): BarStyle {
		
		@Composable
		override fun barColor(selected: Boolean): State<Color> {
			return rememberUpdatedState(
				newValue = if (selected) selectedBarColor else unSelectedBarColor
			)
		}
		
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
		override fun startPaddingBarContainer(selected: Boolean): State<Dp> {
			return rememberUpdatedState(
				newValue = if (selected) selectedStartPaddingBarContainer else unSelectedStartPaddingBarContainer
			)
		}
		
		@Composable
		override fun endPaddingBarContainer(selected: Boolean): State<Dp> {
			return rememberUpdatedState(
				newValue = if (selected) selectedEndPaddingBarContainer else unSelectedEndPaddingBarContainer
			)
		}
		
		@Composable
		override fun showXAxisLine(selected: Boolean): State<Boolean> {
			return rememberUpdatedState(
				newValue = if (selected) selectedShowXAxisLine else unSelectedShowXAxisLine
			)
		}
		
		@Composable
		override fun xAxisLineColor(selected: Boolean): State<Color> {
			return rememberUpdatedState(
				newValue = if (selected) selectedXAxisLineColor else unSelectedXAxisLineColor
			)
		}
		
		@Composable
		override fun xAxisTextStyle(selected: Boolean): State<TextStyle> {
			return rememberUpdatedState(
				newValue = if (selected) selectedXAxisTextStyle else unSelectedXAxisTextStyle
			)
		}
		
		@Composable
		override fun xAxisLineAnimationSpec(selected: Boolean): State<AnimationSpec<Float>> {
			return rememberUpdatedState(
				newValue = if (selected) selectedXAxisLineAnimationSpec else unSelectedXAxisLineAnimationSpec
			)
		}
		
		@Composable
		override fun showYAxisLine(selected: Boolean): State<Boolean> {
			return rememberUpdatedState(
				newValue = if (selected) selectedShowYAxisLine else unSelectedShowYAxisLine
			)
		}
		
		@Composable
		override fun yAxisLineColor(selected: Boolean): State<Color> {
			return rememberUpdatedState(
				newValue = if (selected) selectedYAxisLineColor else unSelectedYAxisLineColor
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
