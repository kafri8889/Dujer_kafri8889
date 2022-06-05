package com.anafthdev.dujer.foundation.uiextension

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.foundation.common.DelayManager
import com.anafthdev.dujer.foundation.common.EventCountdownTimer
import kotlinx.coroutines.launch

/**
 * use [aspectRatio]
 */
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
	
	val delayManager by remember {
		mutableStateOf(
			DelayManager(),
			referentialEqualityPolicy()
		)
	}
	
	val isTimerRunning by delayManager.isRunning.collectAsState()
	
	if (autoRestart) {
		DisposableEffect(state.isScrollInProgress and (state.value != 0)) {
			if (!isTimerRunning) {
				delayManager.delay(
					timeInMillis = autoRestartDelay,
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
