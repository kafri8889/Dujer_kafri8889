package com.anafthdev.dujer.foundation.extension

inline fun <T> Iterable<T>.sumOf(selector: (T) -> Float): Float {
	var sum = 0f
	for (element in this) {
		sum += selector(element)
	}
	
	return sum
}
