package com.anafthdev.dujer.model

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable

data class SettingPreference(
	val title: String,
	val summary: String,
	var value: Any,
	val category: String = "",
	@DrawableRes val iconResId: Int? = null,
	val showValue: Boolean = false,
	val type: PreferenceType = PreferenceType.BASIC
) {
	data class Custom(
		val category: String = "",
		val iconContent: @Composable (ColumnScope.() -> Unit?)?,
		val titleContent: @Composable ColumnScope.() -> Unit,
		val valueContent: @Composable (ColumnScope.() -> Unit)?,
		val onClick: () -> Unit
	)
	
	sealed class PreferenceType(val type: String) {
		object BASIC: PreferenceType("basic")
		object SWITCH: PreferenceType("switch")
		object CUSTOM: PreferenceType("custom")
	}
}
