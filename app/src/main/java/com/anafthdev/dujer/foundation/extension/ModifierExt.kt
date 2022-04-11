package com.anafthdev.dujer.foundation.extension

import androidx.annotation.FloatRange
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.common.EventCountdownTimer
import com.anafthdev.dujer.util.AppUtil.toast
import kotlinx.coroutines.launch

fun Modifier.sizeBasedWidth(
	enlargement: Float = 1f
): Modifier = composed {
	val density = LocalDensity.current
	var width by remember { mutableStateOf(0.dp) }
	
	onGloballyPositioned {
		width = with(density) {
			it.size.width
				.times(enlargement)
				.toDp()
		}
	}.size(width)
}

fun Modifier.horizontalScroll(
	state: ScrollState,
	autoRestartDelay: Long = 3000,
	restartAnimationDuration: Int = 600,
	autoRestart: Boolean = true
): Modifier = composed {
	
	val scope = rememberCoroutineScope()
	
	val eventCountdownTimer by remember { mutableStateOf(EventCountdownTimer()) }
	
	if (autoRestart) {
		DisposableEffect(state.isScrollInProgress) {
			if (!eventCountdownTimer.isTimerRunning) {
				eventCountdownTimer.startTimer(
					millisInFuture = autoRestartDelay,
					onTick = {},
					onFinish = {
						scope.launch {
							state.animateScrollTo(
								value = 0,
								animationSpec = tween(restartAnimationDuration)
							)
						}
					}
				)
			}
			onDispose {}
		}
	}
	
	horizontalScroll(state)
}
