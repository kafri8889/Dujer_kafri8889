package com.anafthdev.dujer.foundation.extension

fun <T> Array<T>.get(predicate: (T) -> Boolean): T? {
	this.forEach {
		if (predicate(it)) return it
	}
	
	return null
}

fun <T> Array<T>.indexOf(predicate: (T) -> Boolean): Int {
	this.forEachIndexed { i, t ->
		if (predicate(t)) return i
	}
	
	return -1
}
