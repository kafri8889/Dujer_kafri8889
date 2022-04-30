package com.anafthdev.dujer.uicomponent.charting.bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.extension.join
import com.anafthdev.dujer.foundation.extension.toDp
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.uicomponent.charting.bar.components.BarAlignment
import com.anafthdev.dujer.uicomponent.charting.bar.components.YAxisPosition
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarChartDefault
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarDataSet
import com.anafthdev.dujer.uicomponent.charting.bar.data.DefaultBarChartFormatter
import com.anafthdev.dujer.uicomponent.charting.bar.ext.groupBarData
import com.anafthdev.dujer.uicomponent.charting.bar.ext.isInside
import com.anafthdev.dujer.uicomponent.charting.bar.interfaces.BarStyle
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.uicomponent.charting.formatter.ChartFormatter

@Composable
fun BarChart(
	barDataSets: Collection<BarDataSet>,
	modifier: Modifier = Modifier,
	state: BarChartState = rememberBarChartState(),
	style: BarStyle = BarChartDefault.barStyle(),
	formatter: ChartFormatter<BarData> = DefaultBarChartFormatter(),
	barAlignment: BarAlignment = BarAlignment.Start,
	yAxisPosition: YAxisPosition = YAxisPosition.INSIDE,
	showXAxis: Boolean = true,
	showYAxis: Boolean = true
) {
	
	// TODO: kasih style ke bar (masing2)
	
	val density = LocalDensity.current
	
	val yDataPoints = barDataSets.getBy { it.barData }.join().getBy { it.y }
	
	val maxDataPoints = (yDataPoints.maxOrNull() ?: 1f)
	val minDataPoints = (yDataPoints.minOrNull() ?: 0f)
	
	val groupBarData = barDataSets.groupBarData()
	
	val selectedBarDataSet = remember(state.observableSelectedBarDataGroup) {
		state.observableSelectedBarDataGroup
	}
	
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.then(modifier)
	) {
		LazyRow(
			modifier = Modifier
				.height(DEFAULT_BAR_CHART_HEIGHT)
				.align(
					alignment = when (barAlignment) {
						BarAlignment.Start -> Alignment.CenterStart
						BarAlignment.Center -> Alignment.Center
					}
				)
		) {
			items(
				count = groupBarData.size,
				key = { i -> groupBarData[i].hashCode() }
			) { index ->
				
				val barDataSet = groupBarData[index]
				
				val isSelected = selectedBarDataSet == barDataSet.first
				
				Row(
					modifier = Modifier
				) {
					for (barData in barDataSet.second) {
						BarItem(
							isSelected = isSelected,
							style = style,
							groupID = barDataSet.first,
							barData = barData,
							minDataPoints = minDataPoints,
							maxDataPoints = maxDataPoints,
							showXAxis = showXAxis,
							showYAxis = showYAxis,
							yAxisPosition = yAxisPosition,
							formatter = formatter,
							state = state
						)
					}
				}
				
				
			}
		}
	}
}

@Composable
fun BarItem(
	isSelected: Boolean,
	style: BarStyle,
	groupID: Int,
	barData: BarData,
	minDataPoints: Float,
	maxDataPoints: Float,
	showXAxis: Boolean,
	showYAxis: Boolean,
	yAxisPosition: YAxisPosition,
	formatter: ChartFormatter<BarData>,
	state: BarChartState
) {
	
	val density = LocalDensity.current
	
	val barColor by style.barColor(selected = isSelected)
	val barWidth by style.barWidth(selected = isSelected)
	val barShape by style.barShape(selected = isSelected)
	val barContainerWidth by style.barContainerWidth(selected = isSelected)
	val horizontalBarPadding by style.horizontalBarContainerPadding(selected = isSelected)
	val xAxisTextStyle by style.xAxisTextStyle(selected = isSelected)
	val yAxisTextStyle by style.yAxisTextStyle(selected = isSelected)
	
	val animatedBarColor by animateColorAsState(targetValue = barColor)
	val animatedBarWidth by animateDpAsState(targetValue = barWidth)
	val animatedContainerWidth by animateDpAsState(targetValue = barContainerWidth)
	val animatedHorizontalBarPadding by animateDpAsState(targetValue = horizontalBarPadding)
	var barHeight by remember { mutableStateOf(DEFAULT_BAR_HEIGHT) }
	
	val height = barData.y
		.minus(minDataPoints)
		.div(maxDataPoints.minus(minDataPoints))
		.times(256)
		.minus(24)
		.coerceIn(
			minimumValue = 18f,
			maximumValue = 232f
		)
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Bottom,
		modifier = Modifier
			.padding(
				horizontal = animatedHorizontalBarPadding
			)
			.fillMaxHeight()
			.width(animatedContainerWidth)
			.clip(barShape)
			.clickable {
				state.update(
					selectedBarDataGroup = groupID
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
						minHeight = DEFAULT_BAR_HEIGHT
					)
					.height(height.dpScaled)
					.width(animatedBarWidth)
					.clip(barShape)
					.background(animatedBarColor)
					.onGloballyPositioned {
						barHeight = it.size.height.toDp(density)
					}
			) {
				if (showYAxis and yAxisPosition.isInside()) {
					Text(
						text = formatter.formatY(barData.y),
						style = yAxisTextStyle,
						modifier = Modifier
							.padding(
								top = if (barHeight <= DEFAULT_BAR_HEIGHT.plus(4.dpScaled)) 0.dpScaled
								else 4.dpScaled
							)
					)
				}
			}
		}
		
		Spacer(
			modifier = Modifier
				.padding(8.dpScaled)
		)
		
		if (showXAxis) {
			Text(
				text = formatter.formatX(barData.x),
				style = xAxisTextStyle,
				modifier = Modifier
					.defaultMinSize(
						minHeight = 24.dpScaled
					)
			)
		} else {
			Spacer(
				modifier = Modifier
					.height(24.dpScaled)
			)
		}
	}
}

internal val DEFAULT_BAR_CHART_HEIGHT = 256.dpScaled
internal val DEFAULT_BAR_HEIGHT = 18.dpScaled
