package com.anafthdev.dujer.feature.wallet.subscreen

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.theme.shapes
import com.anafthdev.dujer.foundation.window.dpScaled

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EditBalanceBottomSheet(
	wallet: Wallet,
	state: ModalBottomSheetState,
	onCancel: () -> Unit,
	onSave: (Wallet, Financial) -> Unit,
	content: @Composable () -> Unit
) {
	
	val keyboardController = LocalSoftwareKeyboardController.current
	
	LaunchedEffect(state.isVisible) {
		if (!state.isVisible) keyboardController?.hide()
	}
	
	ModalBottomSheetLayout(
		sheetState = state,
		sheetShape = RoundedCornerShape(
			topStart = shapes.medium.topStart,
			topEnd = shapes.medium.topEnd,
			bottomEnd = CornerSize(0.dpScaled),
			bottomStart = CornerSize(0.dpScaled)
		),
		sheetContent = {
			EditWalletBalanceScreen(
				wallet = wallet,
				onCancel = onCancel,
				onSave = onSave
			)
		},
		content = content
	)
}
