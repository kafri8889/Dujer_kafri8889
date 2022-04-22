package com.anafthdev.dujer.ui.app.environment

import kotlinx.coroutines.CoroutineDispatcher

interface IDujerEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun vibrate(millis: Long)
	
}