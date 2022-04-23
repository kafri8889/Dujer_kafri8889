package com.anafthdev.dujer.ui.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.preference.Language
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.localized.LocalizedViewModel
import com.anafthdev.dujer.foundation.uimode.UiModeViewModel
import com.anafthdev.dujer.foundation.uimode.data.UiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.SettingPreference
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.black04
import com.anafthdev.dujer.ui.theme.shapes
import com.anafthdev.dujer.uicomponent.SettingPreferences
import com.anafthdev.dujer.uicomponent.TopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
	navController: NavController
) {
	
	val context = LocalContext.current
	
	val uiModeViewModel = hiltViewModel<UiModeViewModel>()
	val settingViewModel = hiltViewModel<SettingViewModel>()
	val localizedViewModel = hiltViewModel<LocalizedViewModel>()
	
	val uiModeState by uiModeViewModel.state.collectAsState()
	val settingState by settingViewModel.state.collectAsState()
	val localizedState by localizedViewModel.state.collectAsState()
	
	val uiMode = uiModeState.uiMode
	val languageUsed = localizedState.language
	val isUseBioAuth = settingState.isUseBioAuth
	
	val scope = rememberCoroutineScope()
	val sheetStateChangeLanguage = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
	
	val hideSheet = {
		scope.launch { sheetStateChangeLanguage.hide() }
		Unit
	}
	val showSheet = {
		scope.launch { sheetStateChangeLanguage.show() }
		Unit
	}
	
	val settingPreferences = listOf(
		SettingPreference(
			title = stringResource(id = R.string.category),
			summary = stringResource(id = R.string.category_summary),
			iconResId = R.drawable.ic_category_2,
			value = "",
			category = stringResource(id = R.string.display_and_configuration)
		),
		SettingPreference(
			title = stringResource(id = R.string.currency),
			summary = stringResource(id = R.string.currency_summary),
			iconResId = R.drawable.ic_dollar_circle,
			value = "",
			category = stringResource(id = R.string.display_and_configuration)
		),
		SettingPreference(
			title = stringResource(id = R.string.language),
			summary = stringResource(id = R.string.language_summary),
			iconResId = R.drawable.ic_language,
			value = "",
			category = stringResource(id = R.string.display_and_configuration)
		),
		SettingPreference(
			title = stringResource(id = R.string.dark_theme),
			summary = "",
			iconResId = if (uiMode.isDarkTheme()) R.drawable.ic_moon else R.drawable.ic_sun,
			value = uiMode.isDarkTheme(),
			category = stringResource(id = R.string.display_and_configuration),
			type = SettingPreference.PreferenceType.SWITCH
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
	
	ModalBottomSheetLayout(
		sheetState = sheetStateChangeLanguage,
		sheetShape = RoundedCornerShape(
			topStart = shapes.medium.topStart,
			topEnd = shapes.medium.topEnd,
			bottomEnd = CornerSize(0.dpScaled),
			bottomStart = CornerSize(0.dpScaled)
		),
		sheetContent = {
			Box(
				modifier = Modifier
					.fillMaxWidth()
			) {
				IconButton(
					onClick = hideSheet,
					modifier = Modifier
						.align(Alignment.CenterStart)
				) {
					Icon(
						imageVector = Icons.Rounded.Close,
						contentDescription = null
					)
				}
				
				Text(
					text = stringResource(id = R.string.select_your_language),
					style = Typography.bodyLarge.copy(
						fontWeight = FontWeight.Medium,
						fontSize = Typography.bodyLarge.fontSize.spScaled
					),
					modifier = Modifier
						.align(Alignment.Center)
				)
			}
			
			for (lang in Language.values()) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							localizedViewModel.setLanguage(lang)
							hideSheet()
						}
						.padding(4.dpScaled)
				) {
					RadioButton(
						selected = languageUsed == lang,
						onClick = {
							localizedViewModel.setLanguage(lang)
							hideSheet()
						}
					)
					
					Text(
						text = lang.name,
						style = Typography.bodyMedium.copy(
							fontWeight = FontWeight.Normal,
							fontSize = Typography.bodyMedium.fontSize.spScaled
						)
					)
				}
			}
			
			Spacer(
				modifier = Modifier
					.fillMaxWidth()
					.height(16.dpScaled)
			)
		}
	) {
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
						0 -> navController.navigate(DujerDestination.Category.createRoute())
						1 -> navController.navigate(DujerDestination.Currency.route)
						2 -> showSheet()
						3 -> uiModeViewModel.setUiMode(
							if (preference.value as Boolean) UiMode.DARK else UiMode.LIGHT
						)
						4 -> {
							settingViewModel.setUseBioAuth(preference.value as Boolean)
						}
					}
				}
			)
		}
	}
}
