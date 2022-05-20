package com.anafthdev.dujer.ui.statistic.data

import android.graphics.Canvas
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import kotlin.math.cos
import kotlin.math.sin

class CustomPieChartRenderer(
	pieChart: PieChart, private val circleRadius: Float
) : PieChartRenderer(pieChart, pieChart.animator, pieChart.viewPortHandler) {
	
	override fun drawValues(c: Canvas) {
		super.drawValues(c)
		
		val center = mChart.centerCircleBox
		
		val radius = mChart.radius
		var rotationAngle = mChart.rotationAngle
		val drawAngles = mChart.drawAngles
		val absoluteAngles = mChart.absoluteAngles
		
		val phaseX = mAnimator.phaseX
		val phaseY = mAnimator.phaseY
		
		val roundedRadius = (radius - radius * mChart.holeRadius / 100f) / 2f
		val holeRadiusPercent = mChart.holeRadius / 100f
		var labelRadiusOffset = radius / 10f * 3.6f
		
		if (mChart.isDrawHoleEnabled) {
			labelRadiusOffset = (radius - radius * holeRadiusPercent) / 2f
			if (!mChart.isDrawSlicesUnderHoleEnabled && mChart.isDrawRoundedSlicesEnabled) {
				rotationAngle += roundedRadius * 360 / (Math.PI * 2 * radius).toFloat()
			}
		}
		
		val labelRadius = radius - labelRadiusOffset
		
		val dataSets = mChart.data.dataSets
		
		var angle: Float
		var xIndex = 0
		
		c.save()
		for (i in dataSets.indices) {
			val dataSet = dataSets[i]
			val sliceSpace = getSliceSpace(dataSet)
			for (j in 0 until dataSet.entryCount) {
				angle = if (xIndex == 0) 0f else absoluteAngles[xIndex - 1] * phaseX
				val sliceAngle = drawAngles[xIndex]
				val sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius)
				angle += (sliceAngle - sliceSpaceMiddleAngle / 2f) / 2f
				
				if (dataSet.valueLineColor != ColorTemplate.COLOR_NONE) {
					val transformedAngle = rotationAngle + angle * phaseY
					val sliceXBase = cos(transformedAngle * Utils.FDEG2RAD.toDouble()).toFloat()
					val sliceYBase = sin(transformedAngle * Utils.FDEG2RAD.toDouble()).toFloat()
					val valueLinePart1OffsetPercentage = dataSet.valueLinePart1OffsetPercentage / 100f
					val line1Radius = if (mChart.isDrawHoleEnabled) {
						(radius - radius * holeRadiusPercent) * valueLinePart1OffsetPercentage + radius * holeRadiusPercent
					} else {
						radius * valueLinePart1OffsetPercentage
					}
					val px = line1Radius * sliceXBase + center.x
					val py = line1Radius * sliceYBase + center.y
					
					if (dataSet.isUsingSliceColorAsValueLineColor) {
						mRenderPaint.color = dataSet.getColor(j)
					}
					c.drawCircle(px, py, circleRadius, mRenderPaint)
				}
				
				xIndex++
			}
		}
		MPPointF.recycleInstance(center)
		c.restore()
	}
}