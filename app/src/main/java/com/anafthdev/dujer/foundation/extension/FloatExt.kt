package com.anafthdev.dujer.foundation.extension

import kotlin.math.roundToInt

/**
 * convert to percent
 *
 * 12.5 percentOf 5 = 40.0%
 *
 * 14.6 percentOf 4.2 = 28.7671%
 */
fun Float.percentOf(value: Float, ifNaN: (() -> Float)? = null): Float {
	val percent = value.div(this).times(100)
	return if ((ifNaN != null) and percent.isNaN()) ifNaN!!() else percent
}

fun Float.percentOf(value: Int, ifNaN: (() -> Float)? = null): Float {
	val percent = value.div(this).times(100)
	return if ((ifNaN != null) and percent.isNaN()) ifNaN!!() else percent
}

fun Float.roundPercentOf(value: Float): Int {
	return value.div(this).times(100).roundToInt()
}

fun Float.roundPercentOf(value: Int): Int {
	return value.div(this).times(100).roundToInt()
}
