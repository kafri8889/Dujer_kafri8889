package com.anafthdev.dujer.uicomponent.charting.bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.extension.join
import com.anafthdev.dujer.foundation.extension.toDp
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.uicomponent.charting.bar.components.BarAlignment
import com.anafthdev.dujer.uicomponent.charting.bar.components.XAxisVisibility
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarChartDefault
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarDataSet
import com.anafthdev.dujer.uicomponent.charting.bar.data.DefaultBarChartFormatter
import com.anafthdev.dujer.uicomponent.charting.bar.ext.groupBarData
import com.anafthdev.dujer.uicomponent.charting.bar.ext.isInside
import com.anafthdev.dujer.uicomponent.charting.bar.ext.isJoined
import com.anafthdev.dujer.uicomponent.charting.bar.ext.isSeparated
import com.anafthdev.dujer.uicomponent.charting.bar.interfaces.BarChartStyle
import com.anafthdev.dujer.uicomponent.charting.bar.interfaces.BarStyle
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.uicomponent.charting.formatter.ChartFormatter
import timber.log.Timber

@Composable
fun BarChart(
	barDataSets: Collection<BarDataSet>,
	modifier: Modifier = Modifier,
	state: BarChartState = rememberBarChartState(),
	style: List<BarStyle> = listOf(BarChartDefault.barStyle()),
	formatter: ChartFormatter<BarData> = DefaultBarChartFormatter(),
	chartStyle: BarChartStyle = BarChartDefault.barChartStyle()
) {
	
	val density = LocalDensity.current
	
	val yDataPoints = barDataSets.getBy { it.barData }.join().getBy { it.y }
	
	val maxDataPoints = (yDataPoints.maxOrNull() ?: 1f)
	val minDataPoints = (yDataPoints.minOrNull() ?: 0f)
	
	val barDataGroup = barDataSets.groupBarData()
	
	val selectedBarDataSet = remember(state.observableSelectedBarDataGroup) {
		state.observableSelectedBarDataGroup
	}
	
	val showXAxis by chartStyle.showXAxis()
	val showYAxis by chartStyle.showYAxis()
	val xAxisVisibility by chartStyle.xAxisVisibility()
	val yAxisPosition by chartStyle.yAxisPosition()
	val barAlignment by chartStyle.barAlignment()
	
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
				count = barDataGroup.size,
				key = { i -> barDataGroup[i].hashCode() }
			) { index ->
				
				val dataGroup = barDataGroup[index]
				
				val isSelected = selectedBarDataSet == dataGroup.first
				Timber.i("i: ${dataGroup.first}, group: ${dataGroup.second}")
				
				val interactionSource = remember { MutableInteractionSource() }
				
				val barShape by style[0].barShape(selected = isSelected)
				
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier
						.defaultMinSize(
							minHeight = DEFAULT_BAR_CHART_HEIGHT + DEFAULT_X_AXIS_HEIGHT
						)
						.clip(barShape)
						.fillMaxHeight()
						.clickable(
							enabled = false,
							indication = LocalIndication.current,
							interactionSource = interactionSource,
							onClick = {
								state.update(
									selectedBarDataGroup = dataGroup.first
								)
							}
						)
				) {
					Row(
						horizontalArrangement = Arrangement.Center,
						modifier = Modifier
							.weight(1f)
					) {
						dataGroup.second.forEachIndexed { j, barData ->
							Timber.i("j: $j")
							
							val height = barData.y
								.minus(minDataPoints)
								.div(maxDataPoints.minus(minDataPoints))
								.times(256)
								.minus(24)
								.coerceIn(
									minimumValue = 18f,
									maximumValue = 232f
								).dpScaled
							
							BarItem(
								isSelected = isSelected,
								style = style[j],
								groupID = dataGroup.first,
								barData = barData,
								height = height,
								interactionSource = if (xAxisVisibility.isJoined()) interactionSource else MutableInteractionSource(),
								showXAxis = showXAxis and xAxisVisibility.isSeparated(),
								showYAxis = showYAxis and yAxisPosition.isInside(),
								xAxisVisibility = xAxisVisibility,
								formatter = formatter,
								onBarClicked = {
									state.update(
										selectedBarDataGroup = dataGroup.first
									)
								}
							)
						}
					}
					
					if (showXAxis and xAxisVisibility.isJoined()) {
						val xAxisTextStyle by style[0].xAxisTextStyle(selected = isSelected)

						Text(
							text = formatter.formatX(dataGroup.second[0].x),
							textAlign = TextAlign.Center,
							style = xAxisTextStyle,
							modifier = Modifier
								.defaultMinSize(
									minHeight = DEFAULT_X_AXIS_HEIGHT
								)
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
	height: Dp,
	interactionSource: MutableInteractionSource,
	showXAxis: Boolean,
	showYAxis: Boolean,
	xAxisVisibility: XAxisVisibility,
	formatter: ChartFormatter<BarData>,
	onBarClicked: () -> Unit
) {
	
	val density = LocalDensity.current
	
	val barColor by style.barColor(selected = isSelected)
	val barWidth by style.barWidth(selected = isSelected)
	val barShape by style.barShape(selected = isSelected)
	val barContainerWidth by style.barContainerWidth(selected = isSelected)
	val startPadding by style.startPaddingBarContainer(selected = isSelected)
	val endPadding by style.endPaddingBarContainer(selected = isSelected)
	val showXAxisLine by style.showXAxisLine(selected = isSelected)
	val showYAxisLine by style.showYAxisLine(selected = isSelected)
	val xAxisLineColor by style.xAxisLineColor(selected = isSelected)
	val xAxisLineAnimationSpec by style.xAxisLineAnimationSpec(selected = isSelected)
	val yAxisLineColor by style.yAxisLineColor(selected = isSelected)
	val xAxisTextStyle by style.xAxisTextStyle(selected = isSelected)
	val yAxisTextStyle by style.yAxisTextStyle(selected = isSelected)
	
	val animatedBarColor by animateColorAsState(
		targetValue = barColor,
		animationSpec = spring(
			stiffness = Spring.StiffnessMedium
		)
	)
	val animatedBarWidth by animateDpAsState(targetValue = barWidth)
	val animatedContainerWidth by animateDpAsState(targetValue = barContainerWidth)
	val animatedStartPaddingBarContainer by animateDpAsState(targetValue = startPadding)
	val animatedEndPaddingBarContainer by animateDpAsState(targetValue = endPadding)
	val animatedXAxisLineWidth by animateFloatAsState(
		targetValue = if (showXAxisLine) 1f else 0f,
		animationSpec = xAxisLineAnimationSpec
	)
	
	var barHeight by remember { mutableStateOf(DEFAULT_BAR_HEIGHT) }
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Bottom,
		modifier = Modifier
			.width(animatedContainerWidth +
					animatedStartPaddingBarContainer +
					animatedEndPaddingBarContainer
			)
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Bottom,
			modifier = Modifier
				.padding(
					start = animatedStartPaddingBarContainer,
					end = animatedEndPaddingBarContainer
				)
				.weight(1f)
				.clip(barShape)
				.clickable(
					enabled = true,
					indication = if (xAxisVisibility.isSeparated()) rememberRipple() else null,
					interactionSource = interactionSource,
					onClick = onBarClicked
				)
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
						.height(height)
						.width(animatedBarWidth)
						.clip(barShape)
						.background(animatedBarColor)
						.onGloballyPositioned {
							barHeight = it.size.height.toDp(density)
						}
				) {
					if (showYAxis) {
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
		}
		
		Divider(
			color = xAxisLineColor,
			thickness = 1.dpScaled,
			modifier = Modifier
				.padding(
					top = 6.dpScaled,
					bottom = 6.dpScaled
				)
				.fillMaxWidth(
					fraction = animatedXAxisLineWidth
				)
		)
		
		if (showXAxis) {
			Text(
				text = formatter.formatX(barData.x),
				textAlign = TextAlign.Center,
				style = xAxisTextStyle,
				modifier = Modifier
					.defaultMinSize(
						minHeight = DEFAULT_X_AXIS_HEIGHT
					)
			)
		}
	}
}

internal val DEFAULT_BAR_CHART_HEIGHT = 256.dpScaled
internal val DEFAULT_BAR_HEIGHT = 18.dpScaled
internal val DEFAULT_X_AXIS_HEIGHT = 24.dpScaled
