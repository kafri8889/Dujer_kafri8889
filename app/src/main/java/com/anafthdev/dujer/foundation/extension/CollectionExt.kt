package com.anafthdev.dujer.foundation.extension

fun <T> Int.lastIndexOf(list: Collection<T>) = this == list.size - 1

fun <T> Collection<T>.toArrayList(): ArrayList<T> = ArrayList(this)

/**
 * list = [[1, 2, 3], [1, 2, 3]]
 * group -> [[1, 1], [2, 2], [3, 3]]
 */
fun <T> Collection<Collection<T>>.transpose(): List<List<T>> {
	val result = arrayListOf<List<T>>()

	val size = this.size
	val length = this[0].size

	require(this.all { it.size == length }) {
		"the size of each element must be the same"
	}

	for (i in 0 until length) {
		val inner = arrayListOf<T>()
		for (j in 0 until size) {
			inner.add(this[j][i])
		}

		result.add(inner)
	}

	return result
}

/**
 * get element from collection
 */
operator fun <T> Collection<T>.get(index: Int): T {
	this.forEachIndexed { i, t -> if (i == index) return t }
	
	throw IndexOutOfBoundsException("no matching index -> size: ${this.size}, index: $index")
}

/**
 *
 */
fun <T> Collection<Collection<T>>.join(): List<T> {
	val result = arrayListOf<T>()
	this.forEach { collection ->
		collection.forEach { result.add(it) }
	}
	
	return result
}

/**
 *
 */
fun <T> Collection<Array<out T>>.joinArray(): List<T> {
	val result = arrayListOf<T>()
	this.forEach { collection ->
		collection.forEach { result.add(it) }
	}
	
	return result
}

/**
 * merge two lists
 * @param other another list
 */
fun <T> Collection<T>.merge(vararg other: List<T>): List<T> {
	return this.toMutableList().apply { other.forEach { addAll(it) } }
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
