package com.anafthdev.dujer.ui.setting

sealed class SettingAction {
	data class SetUseBioAuth(val isUseBioAuth: Boolean): SettingAction()
}
