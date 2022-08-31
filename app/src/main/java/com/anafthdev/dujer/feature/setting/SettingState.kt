package com.anafthdev.dujer.feature.setting

import android.os.Bundle

data class SettingState(
	val isUseBioAuth: Boolean = false,
	val exportFinancialDataBundle: Bundle = Bundle.EMPTY
)
