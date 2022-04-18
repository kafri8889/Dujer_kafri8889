package com.anafthdev.dujer.ui.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.SettingPreference
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.black04
import com.anafthdev.dujer.uicomponent.SettingPreferences
import com.anafthdev.dujer.uicomponent.TopAppBar

@Composable
fun SettingScreen(
	navController: NavController
) {
	
	val context = LocalContext.current
	
	val settingViewModel = hiltViewModel<SettingViewModel>()
	
	val isUseBioAuth by settingViewModel.appDatastore.isUseBioAuth.collectAsState(initial = false)
	
	val settingPreferences = listOf(
		SettingPreference(
			title = stringResource(id = R.string.category),
			summary = stringResource(id = R.string.category_summary),
			iconResId = R.drawable.ic_category_2,
			value = "",
			category = stringResource(id = R.string.configuration)
		),
		SettingPreference(
			title = stringResource(id = R.string.biometric_authentication),
			summary = stringResource(id = R.string.biometric_authentication_summary),
			iconResId = R.drawable.ic_finger_scan,
			value = isUseBioAuth,
			category = stringResource(id = R.string.security),
			type = SettingPreference.PreferenceType.SWITCH
		)
	)
	
	BackHandler {
		navController.popBackStack()
	}
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.systemBarsPadding()
	) {
		TopAppBar {
			IconButton(
				onClick = {
					navController.popBackStack()
				},
				modifier = Modifier
					.padding(start = 8.dpScaled)
					.align(Alignment.CenterStart)
			) {
				Icon(
					imageVector = Icons.Rounded.ArrowBack,
					tint = black04,
					contentDescription = null
				)
			}
			
			Text(
				text = stringResource(id = R.string.setting),
				style = Typography.titleLarge.copy(
					fontWeight = FontWeight.Bold,
					fontSize = Typography.titleLarge.fontSize.spScaled
				)
			)
		}
		
		SettingPreferences(
			preferences = settingPreferences,
			onClick = { preference ->
				val indexSettingPreference = settingPreferences.indexOf {
					it.title == preference.title
				}
				
				when (indexSettingPreference) {
					0 -> {
						navController.navigate(DujerDestination.Category.route) {
							launchSingleTop = true
						}
					}
					1 -> {
						settingViewModel.appDatastore.setUseBioAuth(preference.value as Boolean) {}
					}
				}
			}
		)
	}
}