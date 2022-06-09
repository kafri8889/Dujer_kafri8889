package com.anafthdev.dujer.foundation.extension

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> SnapshotStateList<T>.replace(newList: List<T>){
	clear()
	addAll(newList)
}
