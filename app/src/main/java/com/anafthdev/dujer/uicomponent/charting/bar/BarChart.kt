package com.anafthdev.dujer.uicomponent.charting.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.anafthdev.dujer.foundation.extension.get
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.uicomponent.charting.bar.components.BarAlignment
import com.anafthdev.dujer.uicomponent.charting.bar.components.XAxisPosition
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarChartDefault
import com.anafthdev.dujer.uicomponent.charting.bar.data.DefaultBarChartFormatter
import com.anafthdev.dujer.uicomponent.charting.bar.interfaces.BarColors
import com.anafthdev.dujer.uicomponent.charting.bar.interfaces.BarStyle
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.uicomponent.charting.formatter.ChartFormatter

@Composable
fun BarChart(
	barData: Collection<BarData>,
	modifier: Modifier = Modifier,
	state: BarChartState = rememberBarChartState(),
	colors: BarColors = BarChartDefault.barColor(),
	style: BarStyle = BarChartDefault.barStyle(),
	formatter: ChartFormatter<BarData> = DefaultBarChartFormatter(),
	barAlignment: BarAlignment = BarAlignment.Start,
	xAxisPosition: XAxisPosition = XAxisPosition.INSIDE
) {
	
	val selectedBarData = remember(state.observableSelectedBarDataState) { state.observableSelectedBarDataState }
	
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.then(modifier)
	) {
		LazyRow(
			modifier = Modifier
				.height(DEFAULT_BAR_HEIGHT)
				.align(
					alignment = when (barAlignment) {
						BarAlignment.Start -> Alignment.CenterStart
						BarAlignment.Center -> Alignment.Center
					}
				)
		) {
			items(
				count = barData.size,
				key = { i -> barData.get(i).hashCode() }
			) { index ->
				
				val bar = barData.get(index)
				
				val isSelected = selectedBarData.x == bar.x
				
				val barColor by colors.barColor(selected = isSelected)
				
				val barWidth by style.barWidth(selected = isSelected)
				val barShape by style.barShape(selected = isSelected)
				val barContainerWidth by style.barContainerWidth(selected = isSelected)
				val horizontalBarPadding by style.horizontalBarContainerPadding(selected = isSelected)
				val xAxisTextStyle by style.xAxisTextStyle(selected = isSelected)
				val yAxisTextStyle by style.yAxisTextStyle(selected = isSelected)
				
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Bottom,
					modifier = Modifier
						.padding(
							horizontal = horizontalBarPadding
						)
						.fillMaxHeight()
						.width(barContainerWidth)
						.clip(barShape)
						.clickable {
							state.update(
								selectedBarData = bar
							)
						}
				) {
					
					Box(
						contentAlignment = Alignment.BottomCenter,
						modifier = Modifier
							.weight(1f)
					) {
						Box(
							contentAlignment = Alignment.TopCenter,
							modifier = Modifier
								.defaultMinSize(
									minHeight = 18.dpScaled
								)
//								.height() Todo: kalkulasi heightnya
								.width(barWidth)
								.clip(barShape)
								.background(barColor)
						) {
							Text(
								text = formatter.formatY(bar.y),
								style = yAxisTextStyle
							)
						}
					}
					
					Spacer(
						modifier = Modifier
							.padding(8.dpScaled)
					)
					
					Text(
						text = formatter.formatX(bar.x),
						style = xAxisTextStyle
					)
				}
			}
		}
	}
}

internal val DEFAULT_BAR_HEIGHT = 256.dpScaled
