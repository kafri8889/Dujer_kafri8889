package com.anafthdev.dujer.foundation.uicomponent.charting.formatter

import com.anafthdev.dujer.foundation.uicomponent.charting.bar.model.BarData

abstract class ChartFormatter<T> {
	
	abstract fun formatX(x: Float, data: List<BarData>): String
	
	abstract fun formatY(y: Float, data: List<BarData>): String
	
}