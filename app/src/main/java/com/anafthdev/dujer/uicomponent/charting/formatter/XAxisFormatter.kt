package com.anafthdev.dujer.uicomponent.charting.formatter

import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData

interface XAxisFormatter {
	
	fun format(x: Float, data: List<BarData>): String
	
}