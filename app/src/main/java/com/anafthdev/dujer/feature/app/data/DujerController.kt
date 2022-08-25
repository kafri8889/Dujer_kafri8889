package com.anafthdev.dujer.feature.app.data

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.anafthdev.dujer.foundation.common.BaseEffect

interface DujerController {
	
	fun sendEffect(effect: BaseEffect)
	
}

val LocalDujerController: ProvidableCompositionLocal<DujerController> = compositionLocalOf {
	object : DujerController {
		override fun sendEffect(effect: BaseEffect) {}
	}
}
