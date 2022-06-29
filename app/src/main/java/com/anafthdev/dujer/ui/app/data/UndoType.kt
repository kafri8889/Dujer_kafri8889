package com.anafthdev.dujer.ui.app.data

sealed interface UndoType {
	object Category: UndoType
	object Financial: UndoType
}