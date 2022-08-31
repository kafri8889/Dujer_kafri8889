package com.anafthdev.dujer.foundation.uicomponent.charting.bar.ext

import com.anafthdev.dujer.foundation.uicomponent.charting.bar.components.XAxisVisibility

fun XAxisVisibility.isJoined(): Boolean = this == XAxisVisibility.Joined

fun XAxisVisibility.isSeparated(): Boolean = this == XAxisVisibility.Separated
