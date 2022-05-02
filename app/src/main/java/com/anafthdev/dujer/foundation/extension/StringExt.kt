package com.anafthdev.dujer.foundation.extension

/**
 * if this string not equals s return default
 */
fun String.notEquals(s: String, ignoreCase: Boolean = false, default: () -> String): String {
	return if (!this.equals(s, ignoreCase)) default() else this
}

/**
 * if this string equals s return default
 */
fun String.equals(s: String, ignoreCase: Boolean = false, default: () -> String): String {
	return if (this.equals(s, ignoreCase)) default() else this
}

/**
 * Returns a new string with all occurrences of oldValue replaced with newValue.
 */
fun String.replace(
	oldValue: List<String>,
	newValue: String,
	ignoreCase: Boolean = false
): String {
	var result = this
	
	oldValue.forEach { s ->
		if (this.contains(s)) {
			result = result.replace(s, newValue, ignoreCase)
		}
	}
	
	return result
}

/**
 * Returns `true` if this string starts with the specified prefix.
 */
fun String.startsWith(
	prefix: List<String>,
	ignoreCase: Boolean = false
): Boolean {
	var result = false
	
	prefix.forEach { s ->
		if (this.startsWith(s, ignoreCase) and !result) {
			result = true
		}
	}
	
	return result
}

/**
 * Replace the first char of this string
 */
fun String.replaceFirstChar(
	newValue: String
): String {
	var result = ""
	
	if (this.isNotEmpty()) {
		result = "$newValue${this.removePrefix(this[0].toString())}"
	}
	
	return result
}

fun String.removeFirstAndLastWhitespace(): String {
	
	var result = ""
	var firstCharContainWhitespace = false
	var lastCharContainWhitespace = false
	
	for (i in this.indices) {
		if (!this[i].isWhitespace()) {
			firstCharContainWhitespace = true
			result = this.substring(i, this.length).reversed()
			break
		}
	}
	
	result = if (!firstCharContainWhitespace) result.reversed() else result
	
	for (i in result.indices) {
		if (!result[i].isWhitespace()) {
			lastCharContainWhitespace = true
			result = result.substring(i, result.length).reversed()
			break
		}
	}
	
	return if (lastCharContainWhitespace) result else result.reversed()
}
