package com.anafthdev.dujer.uicomponent.charting.bar.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import com.anafthdev.dujer.uicomponent.charting.toColor

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
	
}