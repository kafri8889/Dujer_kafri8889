package com.anafthdev.dujer.foundation.extension

import kotlin.math.roundToInt

/**
 * convert to percent
 *
 * 12 percentOf 5 = 41.67%
 *
 * 14 percentOf 4 = 28.57%
 */
fun Int.percentOf(value: Float): Float {
	return value.div(this).times(100)
}

fun Int.roundPercentOf(value: Float): Int {
	return value.div(this).times(100).roundToInt()
}
