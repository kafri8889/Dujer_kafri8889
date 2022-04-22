package com.anafthdev.dujer.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.anafthdev.dujer.foundation.window.dpScaled

val full_shape = RoundedCornerShape(100)
val half_full_shape = RoundedCornerShape(50)
val quarter_shape = RoundedCornerShape(25)
val extra_small_shape = RoundedCornerShape(4.dpScaled)
val semi_small_shape = RoundedCornerShape(6.dpScaled)
val small_shape = RoundedCornerShape(8.dpScaled)
val medium_shape = RoundedCornerShape(14.dpScaled)
val large_shape = RoundedCornerShape(18.dpScaled)
val semi_large_shape = RoundedCornerShape(22.dpScaled)
val extra_large_shape = RoundedCornerShape(28.dpScaled)

val shapes = Shapes(
	extraSmall = extra_small_shape,
	small = small_shape,
	medium = medium_shape,
	large = large_shape,
	extraLarge = extra_large_shape
)
