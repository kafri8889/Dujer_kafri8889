package com.anafthdev.dujer.feature.setting

sealed class SettingAction {
	data class SetUseBioAuth(val isUseBioAuth: Boolean): SettingAction()
}
