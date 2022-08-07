package com.anafthdev.dujer.foundation.uicomponent.charting.formatter

import com.anafthdev.dujer.foundation.uicomponent.charting.bar.model.BarData

interface XAxisFormatter {
	
	fun format(x: Float, data: List<BarData>): String
	
}