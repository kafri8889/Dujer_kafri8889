package com.anafthdev.dujer.uicomponent.charting.formatter

abstract class ChartFormatter<T> {
	
	abstract fun formatX(x: Float): String
	
	abstract fun formatY(y: Float): String
	
}