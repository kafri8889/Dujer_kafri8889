package com.anafthdev.dujer.foundation.localized

import com.anafthdev.dujer.data.preference.Language

sealed class LocalizedAction {
	data class SetLanguage(val lang: Language): LocalizedAction()
}