package com.anafthdev.dujer.foundation.extension

inline fun <reified T> T.toArray(): Array<out T> {
	return arrayOf(this)
}
