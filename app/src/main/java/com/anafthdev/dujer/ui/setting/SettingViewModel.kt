package com.anafthdev.dujer.ui.setting

import androidx.lifecycle.ViewModel
import com.anafthdev.dujer.data.datastore.AppDatastore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
	val appDatastore: AppDatastore
): ViewModel() {
	

}