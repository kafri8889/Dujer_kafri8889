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
import com.anafthdev.dujer.feature.add_wallet.AddWalletEffect
import com.anafthdev.dujer.feature.app.data.DujerController
import com.anafthdev.dujer.feature.app.data.LocalDujerController
import com.anafthdev.dujer.feature.theme.*
import com.anafthdev.dujer.foundation.common.AppUtil.toast
import com.anafthdev.dujer.foundation.common.BaseEffect
import com.anafthdev.dujer.foundation.extension.containBy
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.extension.isLightTheme
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uicomponent.CustomSnackbar
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
fun DujerApp(
	viewModel: DujerViewModel
) {
	
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	
	val uiModeViewModel = hiltViewModel<UiModeViewModel>()
	
	val state by viewModel.state.collectAsState()
	val uiModeState by uiModeViewModel.state.collectAsState()
	
	val isSystemInDarkTheme = uiModeState.uiMode.isDarkTheme()
	
	val systemUiController = rememberSystemUiController()
	val snackbarHostState = remember { SnackbarHostState() }
	
	LaunchedEffect(lifecycleOwner) {
		lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			if (!state.allWallet.containBy { it.id == Wallet.cash.id }) {
				viewModel.dispatch(
					DujerAction.InsertWallet(Wallet.cash)
				)
			}
			
			viewModel.dispatch(
				DujerAction.SetController(
					object : DujerController {
						override fun sendEffect(effect: BaseEffect) {
							viewModel.sendEffect(effect)
						}
					}
				)
			)
		}
	}
	
	HandleEffect(
		viewModel = viewModel,
		handle = { effect: BaseEffect ->
			when (effect) {
				is DujerEffect.DeleteFinancial -> {
					snackbarHostState.currentSnackbarData?.dismiss()
					snackbarHostState.showSnackbar(
						message = "${context.getString(R.string.transaction_deleted)} \"${effect.financial.name}\"",
						duration = SnackbarDuration.Short,
						actionLabel = context.getString(R.string.cancel)
					)
				}
				is DujerEffect.DeleteCategory -> {
					snackbarHostState.currentSnackbarData?.dismiss()
					snackbarHostState.showSnackbar(
						message = "${context.getString(R.string.category_deleted)} \"${effect.category.name}\"",
						duration = SnackbarDuration.Short,
						actionLabel = context.getString(R.string.cancel)
					)
				}
				is DujerEffect.DeleteWallet -> {
					snackbarHostState.currentSnackbarData?.dismiss()
					snackbarHostState.showSnackbar(
						message = "${context.getString(R.string.wallet_deleted)} \"${effect.wallet.name}\"",
						duration = SnackbarDuration.Short,
						actionLabel = context.getString(R.string.cancel)
					)
				}
				is AddWalletEffect.WalletCreated -> {
					snackbarHostState.currentSnackbarData?.dismiss()
					snackbarHostState.showSnackbar(
						message = context.getString(R.string.wallet_created),
						duration = SnackbarDuration.Short,
						withDismissAction = true
					)
				}
				is AddWalletEffect.BlankWalletName -> {
					context.getString(R.string.wallet_name_cannot_be_empty).toast(context)
				}
				else -> {}
			}
		}
	)
	
	DujerTheme(
		isSystemInDarkTheme = isSystemInDarkTheme
	) {
		val backgroundColor = MaterialTheme.colorScheme.background
		
		CompositionLocalProvider(
			LocalUiMode provides uiModeState.uiMode,
			LocalUiColor provides LocalUiColor.current.copy(
				headlineText = if (isSystemInDarkTheme) black09 else black02,
				titleText = if (isSystemInDarkTheme) black08 else black03,
				normalText = if (isSystemInDarkTheme) black10 else black01,
				bodyText = if (isSystemInDarkTheme) black08 else black05,
				labelText = if (isSystemInDarkTheme) black06 else black06
			),
			LocalCurrency provides state.currentCurrency,
			LocalDujerState provides state,
			LocalContentColor provides if (isSystemInDarkTheme) black10 else black01,
			LocalDujerController provides state.controller,
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
										DujerAction.Undo(state.dataCanReturned)
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
				SideEffect {
					systemUiController.setSystemBarsColor(
						color = Color.Transparent,
						darkIcons = uiModeState.uiMode.isLightTheme()
					)
					
					systemUiController.setNavigationBarColor(
						color = backgroundColor
					)
				}
				
				DujerNavigation(
					dujerViewModel = viewModel,
					modifier = Modifier
						.fillMaxSize()
				)
			}
		}
	}
}
