package com.anafthdev.dujer.foundation.extension

fun <K, V> Map<K, V>.forEachMap(action: (k: K, v: V) -> Unit) {
	this.forEach { action(it.key, it.value) }
}
