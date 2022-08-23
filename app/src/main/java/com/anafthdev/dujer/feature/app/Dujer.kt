package com.anafthdev.dujer.feature.app

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.app.component.CustomSnackbar
import com.anafthdev.dujer.feature.theme.*
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uimode.UiModeViewModel
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.viewmodel.HandleEffect
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.runtime.navigation.DujerNavigation
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
	ExperimentalFoundationApi::class,
	ExperimentalMaterial3Api::class
)
@Composable
fun DujerApp() {
	
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	
	val viewModel = hiltViewModel<DujerViewModel>()
	val uiModeViewModel = hiltViewModel<UiModeViewModel>()
	
	val state by viewModel.state.collectAsState()
	val uiModeState by uiModeViewModel.state.collectAsState()
	
	val currentCurrency = state.currentCurrency
	val dataCanReturned = state.dataCanReturned
	val uiMode = uiModeState.uiMode
	
	val isSystemInDarkTheme = uiMode.isDarkTheme()
	
	val systemUiController = rememberSystemUiController()
	val snackbarHostState = remember { SnackbarHostState() }
	
	LaunchedEffect(lifecycleOwner) {
		lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			viewModel.dispatch(
				DujerAction.InsertWallet(Wallet.cash)
			)
		}
	}
	
	HandleEffect(
		viewModel = viewModel,
		handle = { effect ->
			when (effect) {
				is DujerEffect.DeleteFinancial -> {
					snackbarHostState.currentSnackbarData?.dismiss()
					snackbarHostState.showSnackbar(
						message = "${context.getString(R.string.finance_removed)} \"${effect.financial.name}\"",
						duration = SnackbarDuration.Short
					)
				}
				is DujerEffect.DeleteCategory -> {
					snackbarHostState.currentSnackbarData?.dismiss()
					snackbarHostState.showSnackbar(
						message = "${context.getString(R.string.category_removed)} \"${effect.category.name}\"",
						duration = SnackbarDuration.Short
					)
				}
				else -> {}
			}
		}
	)
	
	SideEffect {
		systemUiController.setSystemBarsColor(
			color = Color.Transparent,
			darkIcons = !isSystemInDarkTheme
		)
	}
	
	DujerTheme(
		isSystemInDarkTheme = isSystemInDarkTheme
	) {
		CompositionLocalProvider(
			LocalUiMode provides uiMode,
			LocalUiColor provides LocalUiColor.current.copy(
				headlineText = if (isSystemInDarkTheme) black09 else black02,
				titleText = if (isSystemInDarkTheme) black08 else black03,
				normalText = if (isSystemInDarkTheme) black10 else black01,
				bodyText = if (isSystemInDarkTheme) black08 else black05,
				labelText = if (isSystemInDarkTheme) black06 else black06
			),
			LocalCurrency provides currentCurrency,
			LocalDujerState provides state,
			LocalContentColor provides if (isSystemInDarkTheme) black10 else black01,
			LocalOverscrollConfiguration provides null
		) {
			Scaffold(
				snackbarHost = {
					SnackbarHost(
						hostState = snackbarHostState,
						snackbar = { snackbarData ->
							CustomSnackbar(
								snackbarData = snackbarData,
								onCancel = {
									viewModel.dispatch(
										DujerAction.Undo(dataCanReturned)
									)
								}
							)
						},
						modifier = Modifier
							.padding(
								vertical = 16.dpScaled,
								horizontal = 8.dpScaled
							)
					)
				}
			) {
				DujerNavigation(
					dujerViewModel = viewModel,
					modifier = Modifier
						.fillMaxSize()
				)
			}
		}
	}
}
