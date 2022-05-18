package com.anafthdev.dujer.uicomponent.charting.pie

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import com.anafthdev.dujer.foundation.extension.sumOf
import com.anafthdev.dujer.foundation.window.DPI
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.uicomponent.charting.pie.model.PieData
import timber.log.Timber
import java.lang.Integer.min
import kotlin.math.atan2

@Composable
fun PieChartPreview() {
	PieChart(
		pieDataList = listOf(
			PieData(
				x = "1",
				y = 200f,
				color = Color.Blue.toArgb()
			),
			PieData(
				x = "2",
				y = 500f,
				color = Color.Magenta.toArgb()
			),
			PieData(
				x = "3",
				y = 600f,
				color = Color.Green.toArgb()
			)
		)
	)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PieChart(
	pieDataList: List<PieData>,
	modifier: Modifier = Modifier,
	state: PieChartState = rememberPieChartState(),
	centerContent: @Composable BoxScope.(Int) -> Unit = {},
) {
	
	if (pieDataList.isEmpty()) return
	
	val density = LocalDensity.current
	
	val totalY = pieDataList.sumOf { it.y }
	
	val proportion = pieDataList.map {
		it.y * 100 / totalY
	}
	
	val sweepAnglePercentage = proportion.map {
		360 * it / 100
	}
	
	val progressSize = remember { mutableListOf<Float>() }
	progressSize.clear()
	progressSize.add(sweepAnglePercentage.first())
	
	for (x in 1 until sweepAnglePercentage.size) {
		progressSize.add(sweepAnglePercentage[x] + progressSize[x - 1])
	}
	
	
	Timber.i("sweepAngle: $sweepAnglePercentage")
	Timber.i("progressSize: $progressSize")
	
	var pieSize by remember { mutableStateOf(256.dpScaled) }
	var selectedPie by remember { mutableStateOf(-1) }
	Timber.i("selectedPie: $selectedPie")
	
	var startAngle = 270f
	
	BoxWithConstraints(
		modifier = modifier
			.fillMaxWidth()
			.onPlaced {
				pieSize = with(density) { it.size.width.toDp() }
			}
			.size(pieSize)
	) {
		val sideSize = min(constraints.maxWidth, constraints.maxHeight)
		val padding = (sideSize * 20) / 100f
		
		val size = Size(sideSize.toFloat() - padding, sideSize.toFloat() - padding)
		
		Canvas(
			modifier = Modifier
				.width(sideSize.dpScaled)
				.height(sideSize.dpScaled)
				.pointerInput(true) {
					detectTapGestures {
						val clickedAngle = convertTouchEventPointToAngle(
							sideSize.toFloat(),
							sideSize.toFloat(),
							it.x,
							it.y
						)
						
						Timber.i("clickedAngle: $clickedAngle, progressSize: $progressSize")
						progressSize.forEachIndexed { index, item ->
							if (clickedAngle <= item) {
//								state.selectedPieData = if (selectedPie != index) index else -1
//								selectedPie = state.selectedPieData
//								Timber.i("selectedPieData: ${state.selectedPieData}")
								selectedPie = if (selectedPie != index) index else -1
								
								return@detectTapGestures
							}
						}
					}
				}
		) {
			sweepAnglePercentage.forEachIndexed { i, sweepAngle ->
				drawPie(
					color = Color(pieDataList[i].color),
					startAngle = startAngle,
					sweepAngle = if (sweepAngle.isNaN()) 360f else sweepAngle,
					size = size,
					padding = padding,
					width = getStrokeWidth(
						isSelected = selectedPie == i,
						dpi = DPI.getDPI(density.density)
					)
				)
				
				startAngle += sweepAngle
			}
		}
		
		if (selectedPie != -1) {
			Box(
				content = {
					centerContent(selectedPie)
				},
				modifier = Modifier
					.align(Alignment.Center)
			)
		}
	}
}

private fun DrawScope.drawPie(
	color: Color,
	startAngle: Float,
	sweepAngle: Float,
	size: Size,
	padding: Float,
	width: Float
) {
	
	// kalkulasi padding
	// misal width = 150 derajat
	// padding = (width / 2) + width
	// padding = 225
//	val padding = (width / 2) + width
	
	drawArc(
		color = color,
		startAngle = startAngle,
		sweepAngle = sweepAngle,
		useCenter = false,
//		size = Size(
//			width = size.width - padding,
//			height = size.width - padding
//		),
		size = size,
		topLeft = Offset(
			x = padding / 2f,
			y = padding / 2f
		),
		style = Stroke(
			width = width
		)
	)
}

private fun convertTouchEventPointToAngle(
	width: Float,
	height: Float,
	xPos: Float,
	yPos: Float
): Double {
	val x = xPos - (width * 0.5f)
	val y = yPos - (height * 0.5f)
	
	var angle = Math.toDegrees(atan2(y.toDouble(), x.toDouble()) + Math.PI / 2)
	angle = if (angle < 0) angle + 360 else angle
	return angle
}

/**
 * get PieChart stroke width
 * @return pie chart stroke width in px
 */
fun getStrokeWidth(isSelected: Boolean, dpi: DPI): Float {
	return when (dpi) {
		is DPI.LDPI -> if (isSelected) 50f else 30f
		is DPI.MDPI -> if (isSelected) 65f else 45f
		is DPI.HDPI -> if (isSelected) 80f else 60f
		is DPI.XHDPI -> if (isSelected) 95f else 75f
		is DPI.XXHDPI -> if (isSelected) 110f else 90f
		is DPI.XXXHDPI -> if (isSelected) 125f else 105f
	}
}
