package com.anafthdev.dujer.ui.setting

import android.Manifest
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
import com.anafthdev.dujer.foundation.common.AppUtil.timeFormatter
import com.anafthdev.dujer.foundation.common.PermissionUtil
import com.anafthdev.dujer.foundation.common.csv.CSVWriter
import com.anafthdev.dujer.foundation.extension.*
import com.anafthdev.dujer.foundation.localized.LocalizedAction
import com.anafthdev.dujer.foundation.localized.LocalizedViewModel
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.UiModeAction
import com.anafthdev.dujer.foundation.uimode.UiModeViewModel
import com.anafthdev.dujer.foundation.uimode.data.UiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.runtime.MainActivity
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.shapes
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.uicomponent.preference.BasicPreference
import com.anafthdev.dujer.uicomponent.preference.SettingPreferences
import com.anafthdev.dujer.uicomponent.preference.SwitchPreference
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
	navController: NavController
) {
	
	val context = LocalContext.current
	val dujerState = LocalDujerState.current
	val localCurrency = LocalCurrency.current
	
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
		BasicPreference(
			title = stringResource(id = R.string.category),
			summary = stringResource(id = R.string.category_summary),
			iconResId = R.drawable.ic_category_2,
			value = "",
			category = stringResource(id = R.string.display_and_configuration)
		),
		BasicPreference(
			title = stringResource(id = R.string.currency),
			summary = stringResource(id = R.string.currency_summary),
			iconResId = R.drawable.ic_dollar_circle,
			value = "",
			category = stringResource(id = R.string.display_and_configuration)
		),
		BasicPreference(
			title = stringResource(id = R.string.language),
			summary = stringResource(id = R.string.language_summary),
			iconResId = R.drawable.ic_language,
			value = "",
			category = stringResource(id = R.string.display_and_configuration)
		),
		SwitchPreference(
			title = stringResource(id = R.string.dark_theme),
			summary = "",
			iconResId = if (uiMode.isDarkTheme()) R.drawable.ic_moon else R.drawable.ic_sun,
			isChecked = uiMode.isDarkTheme(),
			category = stringResource(id = R.string.display_and_configuration),
		),
		SwitchPreference(
			title = stringResource(id = R.string.biometric_authentication),
			summary = stringResource(id = R.string.biometric_authentication_summary),
			iconResId = R.drawable.ic_finger_scan,
			isChecked = isUseBioAuth,
			category = stringResource(id = R.string.security),
		),
		BasicPreference(
			title = stringResource(id = R.string.export),
			summary = CSVWriter.SAVE_DIR.path,
			iconResId = R.drawable.ic_export,
			category = stringResource(id = R.string.other),
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
			Column(
				modifier = Modifier
					.background(
						if (uiMode.isLightTheme()) MaterialTheme.colorScheme.background
						else MaterialTheme.colorScheme.surfaceVariant
					)
			) {
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
								localizedViewModel.dispatch(
									LocalizedAction.SetLanguage(lang)
								)
								
								hideSheet()
							}
							.padding(4.dpScaled)
					) {
						RadioButton(
							selected = languageUsed == lang,
							onClick = {
								localizedViewModel.dispatch(
									LocalizedAction.SetLanguage(lang)
								)
								
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
						contentDescription = null
					)
				}
				
				Text(
					text = stringResource(id = R.string.setting),
					style = MaterialTheme.typography.titleLarge.copy(
						color = LocalUiColor.current.titleText,
						fontWeight = FontWeight.Bold,
						fontSize = Typography.titleLarge.fontSize.spScaled
					)
				)
			}
			
			SettingPreferences(
				preferences = settingPreferences,
				onClick = { preference ->
					val selectedPreferenceIndex = settingPreferences.indexOf {
						it.title == preference.title
					}
					
					when (selectedPreferenceIndex) {
						0 -> navController.navigate(DujerDestination.Category.createRoute())
						1 -> navController.navigate(DujerDestination.Currency.route)
						2 -> showSheet()
						3 -> uiModeViewModel.dispatch(
							UiModeAction.SetUiMode(
								if ((preference as SwitchPreference).isChecked) UiMode.DARK
								else UiMode.LIGHT
							)
						)
						4 -> {
							settingViewModel.dispatch(
								SettingAction.SetUseBioAuth((preference as SwitchPreference).isChecked)
							)
						}
						5 -> {
							val fileName = "financial ${timeFormatter.format(System.currentTimeMillis())}.csv"
							val wallets = dujerState.allWallet
							val financials = dujerState.allIncomeTransaction.merge(dujerState.allExpenseTransaction)
								.sortedBy { it.dateCreated }
							
							if (PermissionUtil.checkPermission(
									context,
									arrayOf(
										Manifest.permission.WRITE_EXTERNAL_STORAGE,
										Manifest.permission.READ_EXTERNAL_STORAGE
									)
								)
							) {
								CSVWriter.writeFinancial(
									context,
									fileName,
									localCurrency,
									wallets,
									financials
								)
							} else {
								(context as MainActivity).exportFinancialDataBundle.apply {
									putString("fileName", fileName)
									putParcelable("currency", localCurrency)
									putParcelableArrayList("wallets", wallets.toArrayList())
									putParcelableArrayList("financials", financials.toArrayList())
								}
								
								context.permissionLauncher.launch(
									arrayOf(
										Manifest.permission.WRITE_EXTERNAL_STORAGE,
										Manifest.permission.READ_EXTERNAL_STORAGE
									)
								)
							}
						}
					}
				}
			)
		}
	}
}
