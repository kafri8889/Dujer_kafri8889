package com.anafthdev.dujer.uicomponent.charting.bar.ext

import com.anafthdev.dujer.uicomponent.charting.bar.components.YAxisPosition

fun YAxisPosition.isInside(): Boolean = this == YAxisPosition.INSIDE

fun YAxisPosition.isOutside(): Boolean = this == YAxisPosition.OUTSIDE
