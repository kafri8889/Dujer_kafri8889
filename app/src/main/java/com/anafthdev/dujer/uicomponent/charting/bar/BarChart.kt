package com.anafthdev.dujer.uicomponent.charting.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import com.anafthdev.dujer.foundation.extension.get
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarAlignment
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarChartDefault
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarColors
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData

@Composable
fun BarChart(
	barData: Collection<BarData>,
	modifier: Modifier = Modifier,
	state: BarChartState = rememberBarChartState(),
	colors: BarColors = BarChartDefault.barColor(),
	barAlignment: BarAlignment = BarAlignment.Start
) {
	
	val selectedBarData = remember(state.observableSelectedBarDataState) { state.observableSelectedBarDataState }
	
	LazyRow(
		modifier = modifier
			.fillMaxWidth()
			.height(DEFAULT_BAR_HEIGHT)
			.composed {
				if (barAlignment == BarAlignment.Start) horizontalScroll(rememberScrollState())
				else this // TODO: center row pake weight ?
			}
	) {
		items(
			count = barData.size,
			key = { i -> barData.get(i).hashCode() }
		) { index ->
			
			val bar = barData.get(index)
			
			val isSelected = selectedBarData.x == bar.x
			
			val barColor by colors.barColor(selected = isSelected)
			
			Column(
				modifier = Modifier
					.fillMaxHeight()
					.width(24.dpScaled)
					.clip(MaterialTheme.shapes.small)
					.clickable {
						state.update(
							selectedBarData = bar
						)
					}
			) {
				Box(
					modifier = Modifier
						.width(16.dpScaled)
						.height(100.dpScaled) // TODO: kalkulasi tingginya
						.clip(MaterialTheme.shapes.small)
						.background(barColor)
				)
			}
		}
	}
}

internal val DEFAULT_BAR_HEIGHT = 256.dpScaled
