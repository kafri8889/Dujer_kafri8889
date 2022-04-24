package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.foundation.uimode.data.UiMode

fun UiMode.isDarkTheme(): Boolean = this == UiMode.DARK

fun UiMode.isLightTheme(): Boolean = this == UiMode.LIGHT
