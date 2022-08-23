package com.anafthdev.dujer.feature.setting

import android.os.Bundle

sealed interface SettingAction {
	data class SetUseBioAuth(val isUseBioAuth: Boolean): SettingAction
	data class SetExportFinancialDataBundle(val bundle: Bundle): SettingAction
}
