package com.anafthdev.dujer.uicomponent.charting.formatter

import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData

interface YAxisFormatter {
	
	fun format(y: Float, data: List<BarData>): String
	
}