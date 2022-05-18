package com.anafthdev.dujer.foundation.window

sealed class DPI(val density: Float) {
	object LDPI: DPI(0.75f)
	object MDPI: DPI(1f)
	object HDPI: DPI(1.5f)
	object XHDPI: DPI(2f)
	object XXHDPI: DPI(3f)
	object XXXHDPI: DPI(4f)
	
	companion object {
		fun getDPI(density: Float): DPI {
			return when (density) {
				in 0f..0.75f -> LDPI
				in 0.75f..1f -> MDPI
				in 1f..1.5f -> HDPI
				in 1.5f..2f -> XHDPI
				in 2f..3f -> XXHDPI
				in 3f..4f -> XXXHDPI
				else -> MDPI
			}
		}
	}
}