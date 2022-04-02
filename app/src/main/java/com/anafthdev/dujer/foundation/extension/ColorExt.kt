package com.anafthdev.dujer.foundation.extension

import android.graphics.ColorSpace
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * convert ARGB color int to [Color]
 */
fun Int.toColor(): Color = Color(this)
