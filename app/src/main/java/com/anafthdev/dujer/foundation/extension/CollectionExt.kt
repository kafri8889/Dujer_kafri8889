package com.anafthdev.dujer.foundation.extension

import java.util.ArrayList

fun <T> Int.lastIndexOf(list: Collection<T>) = this == list.size - 1

/**
 * combine two lists
 * @param other another list
 */
fun <T> Collection<T>.combine(other: List<T>): List<T> {
	return this.toMutableList().apply { addAll(other) }
}

/**
 * Move element.
 * @author kafri8889
 */
fun <T> Collection<T>.move(fromIndex: Int, toIndex: Int): List<T> {
	if (fromIndex == toIndex) return this.toList()
	return ArrayList(this).apply {
		val temp = get(fromIndex)
		removeAt(fromIndex)
		add(toIndex, temp)
	}
}

/**
 * Get index element of given predicate.
 * if element is not in list, return -1
 * @author kafri8889
 */
fun <T> Collection<T>.indexOf(predicate: (T) -> Boolean): Int {
	this.forEachIndexed { i, t ->
		if (predicate(t)) return i
	}
	
	return -1
}

/**
 * Remove element by given [predicate].
 * @author kafri8889
 */
fun <T> Collection<T>.removeBy(predicate: (T) -> Boolean): List<T> {
	val result = ArrayList(this)
	
	this.forEachIndexed { i, t ->
		if (predicate(t)) result.removeAt(i)
	}
	
	return result
}

/**
 * Checks if the specified element is contained in this collection by given [predicate].
 * @author kafri8889
 */
fun <T> Collection<T>.containBy(predicate: (T) -> Boolean): Boolean {
	this.forEach {
		if (predicate(it)) return true
	}
	
	return false
}

/**
 * Return a item containing only elements matching the given [predicate].
 * @author kafri8889
 */
fun <T> Collection<T>.get(predicate: (T) -> Boolean): T? {
	this.forEach {
		if (predicate(it)) return it
	}
	
	return null
}

/**
 * Returns a list element from given collection
 * @author kafri8889
 */
fun <T, U> Collection<T>.getBy(selector: (T) -> U): List<U> {
	val result = ArrayList<U>()
	for (v in this) { result.add(selector(v)) }
	return result
}

/**
 * Convert element
 * @author kafri8889
 */
fun <T, R> Collection<T>.convert(to: (T) -> R): List<R> {
	val result = ArrayList<R>()
	this.forEach { result.add(to(it)) }
	return result
}
