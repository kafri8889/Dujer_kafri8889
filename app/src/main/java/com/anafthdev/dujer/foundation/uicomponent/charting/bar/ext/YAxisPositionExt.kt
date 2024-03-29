package com.anafthdev.dujer.foundation.uicomponent.charting.bar.ext

import com.anafthdev.dujer.foundation.uicomponent.charting.bar.components.YAxisPosition

fun YAxisPosition.isInside(): Boolean = this == YAxisPosition.Inside

fun YAxisPosition.isOutside(): Boolean = this == YAxisPosition.Outside
