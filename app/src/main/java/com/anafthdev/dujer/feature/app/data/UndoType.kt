package com.anafthdev.dujer.feature.app.data

sealed interface UndoType {
	object Category: UndoType
	object Financial: UndoType
}