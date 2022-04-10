package com.anafthdev.dujer.foundation.extension

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
