package com.anafthdev.dujer.foundation.extension

/**
 * Returns a list element from given collection
 * @author kafri8889
 */
fun <T, U> Array<T>.getBy(selector: (T) -> U): List<U> {
	val result = ArrayList<U>()
	for (v in this) { result.add(selector(v)) }
	return result
}

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
