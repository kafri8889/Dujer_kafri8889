package com.anafthdev.dujer.foundation.common

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned

class FocusHandler(
	private val focusManager: FocusManager
	) {
	
	private var currentFocusedTag = ""
	private val touchZones = arrayListOf<TouchZone>()
	
	fun addComponent(touchZone: TouchZone) {
		touchZones.add(touchZone)
	}
	
	fun getComponent(tag: String): TouchZone? {
		return touchZones.find { it.tag == tag }
	}
	
	fun checkGesture(x: Float, y: Float) {
		touchZones.forEach {
			if (!it.inArea(x, y) && currentFocusedTag == it.tag) focusManager.clearFocus()
		}
	}
	
	fun setCurrentFocusedTag(tag: String) {
		currentFocusedTag = tag
	}
}

class TouchZone(
	val tag: String,
	var left: Float,
	var top: Float,
	var right: Float,
	var bottom: Float
) {
	
	constructor(
		tag: String,
		rect: Rect,
	): this(
		tag = tag,
		left = rect.left,
		top = rect.top,
		right = rect.right,
		bottom = rect.bottom,
	)
	
	fun update(rect: Rect) {
		left = rect.left
		top = rect.top
		right = rect.right
		bottom = rect.bottom
	}
	
	fun inArea(x: Float, y: Float): Boolean {
		return x > left && x < right && y > top && y < bottom
	}
}

fun Modifier.freeFocusOnClickOutside(
	tag: String,
	requester: FocusRequester,
	focusHandler: FocusHandler
): Modifier {
	return onGloballyPositioned {
		val touchZone = focusHandler.getComponent(tag)
		
		if (touchZone != null) {
			touchZone.update(it.boundsInParent())
		} else focusHandler.addComponent(TouchZone(tag, it.boundsInParent()))
	}
		.focusRequester(requester)
		.onFocusChanged {
			if (it.hasFocus) focusHandler.setCurrentFocusedTag(tag)
		}
}

fun Modifier.detectGesture(
	focusHandler: FocusHandler
): Modifier {
	return pointerInput(Unit) {
		forEachGesture {
			awaitPointerEventScope {
				
				awaitFirstDown()
				
				do {
					val event: PointerEvent = awaitPointerEvent()
					
					event.changes.forEach { pointerInputChange: PointerInputChange ->
						val pos = pointerInputChange.position
						
						focusHandler.checkGesture(pos.x, pos.y)
						if (pointerInputChange.positionChange() != Offset.Zero) pointerInputChange.consume()
					}
				} while (event.changes.any { it.pressed })
			}
		}
	}
}
