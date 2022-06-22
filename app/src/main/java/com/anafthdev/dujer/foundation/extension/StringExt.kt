package com.anafthdev.dujer.foundation.extension

/**
 * uppercase fist word in string, ex:
 *
 * "abc".uppercaseFirstWord() => "Abc"
 *
 * "abC".uppercaseFirstWord() => "AbC"
 *
 * "abC".uppercaseFirstWord(true) => "AbC"
 *
 * "abC".uppercaseFirstWord(true) => "Abc"
 */
fun String.uppercaseFirstWord(onlyFirstWord: Boolean = false): String {
	if (isBlank()) return this
	if (length == 1) return uppercase()
	
	return if (onlyFirstWord) {
		"${this[0].uppercase()}${this.substring(1, length).lowercase()}"
	} else "${this[0].uppercase()}${this.substring(1, length)}"
}

/**
 * take a string that is not a number
 *
 * ex:
 * val s = "123a456b"
 * s.takeNonDigitString() => "ab"
 * @author kafri8889
 */
fun String.takeNonDigitString(): String {
	val builder = StringBuilder()
	
	forEach { if (!it.isDigit()) builder.append(it) }
	
	return builder.toString()
}

/**
 * take a string that is a number
 *
 * ex:
 * val s = "123a456b"
 * s.takeDigitString() => "123456"
 * @author kafri8889
 */
fun String.takeDigitString(): String {
	var builder = ""
	
	forEach { if (it.isDigit()) builder += it }
	
	return builder
}

/**
 * remove non digit char
 *
 * ex:
 * "123abc456" => "123456"
 */
fun String.removeNonDigitChar(): String {
	val nonDigitChar = mutableSetOf<Char>()
	
	forEach {
		if (!it.isDigit()) nonDigitChar.add(it)
	}
	
	return replace(
		oldValue = nonDigitChar.map { it.toString() },
		newValue = ""
	)
}

/**
 * get start index and end index
 *
 * @return (startIndex, endIndex)
 */
fun String.indexOf(s: String, ignoreCase: Boolean = false): Pair<Int, Int> {
	val startIndex = indexOf(string = s, ignoreCase = ignoreCase)
	val endIndex = startIndex + (s.length - 1)
	
	return startIndex to endIndex
}

//fun String.indexOf(s: String): Pair<Int, Int> {
//    var match = false
//    var matchedCount = 0
//    var firstIndex = -1
//    var lastIndex = -1
//
//    run loop@ {
//        forEachIndexed { cIndex, c ->
//            if (matchedCount == s.length) {
//                lastIndex = cIndex
//                return@loop
//            } else {
//                if (c == s[matchedCount]) {
//                    val matchTemp = match
//                    match = c == s[matchedCount]
//
//                    if (match and !matchTemp) {
//                        firstIndex = cIndex
//                    }
//
//                    matchedCount = if (match) matchedCount + 1 else 0
//                }
//            }
//        }
//    }
//
//    return firstIndex to lastIndex
//}

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
 * check if this string ends with a number
 */
fun String.endsWithNumber(): Boolean = this[this.lastIndex].isDigit()

/**
 * add a new string before the given index
 * @author kafri8889
 */
fun String.addStringBefore(s: String, index: Int): String {
	val result = StringBuilder()
	forEachIndexed { i, c ->
		if (i == index) {
			result.append(s)
			result.append(c)
		} else result.append(c)
	}
	
	return result.toString()
}

/**
 * add a new string before the given index
 */
fun String.addStringAfter(s: String, index: Int): String {
	val result = StringBuilder()
	forEachIndexed { i, c ->
		if (i == index) {
			result.append(c)
			result.append(s)
		} else result.append(c)
	}
	
	return result.toString()
}

/**
 * Returns a new string with all occurrences of oldValue replaced with newValue.
 * @author kafri8889
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
