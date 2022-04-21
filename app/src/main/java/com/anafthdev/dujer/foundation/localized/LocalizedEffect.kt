package com.anafthdev.dujer.foundation.localized

import com.anafthdev.dujer.data.preference.Language

sealed class LocalizedEffect {
	data class ApplyLanguage(val language: Language) : LocalizedEffect()
}