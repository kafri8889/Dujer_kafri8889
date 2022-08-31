package com.anafthdev.dujer.foundation.uicomponent.charting.formatter

import com.anafthdev.dujer.foundation.uicomponent.charting.bar.model.BarData

interface YAxisFormatter {
	
	fun format(y: Float, data: List<BarData>): String
	
}