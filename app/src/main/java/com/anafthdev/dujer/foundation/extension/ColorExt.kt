package com.anafthdev.dujer.foundation.extension

import androidx.compose.ui.graphics.Color

/**
 * convert ARGB color int to [Color]
 */
fun Int.toColor(): Color = Color(this)
