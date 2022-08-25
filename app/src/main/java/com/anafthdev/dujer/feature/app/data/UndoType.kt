package com.anafthdev.dujer.feature.app.data

sealed interface UndoType {
	object Wallet: UndoType
	object Category: UndoType
	object Financial: UndoType
}