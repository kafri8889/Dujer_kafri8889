package com.anafthdev.dujer.ui.app.data

sealed class UndoType {
	object Category: UndoType()
	object Financial: UndoType()
}