package com.anafthdev.dujer.foundation.uimode

import com.anafthdev.dujer.foundation.uimode.data.UiMode

sealed class UiModeAction {
	data class SetUiMode(val uiMode: UiMode): UiModeAction()
}
