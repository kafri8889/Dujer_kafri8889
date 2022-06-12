package com.anafthdev.dujer.uicomponent

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.ui.financial.FinancialScreen
import com.anafthdev.dujer.ui.financial.data.FinancialAction

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FinancialBottomSheet(
	state: ModalBottomSheetState,
	financial: Financial,
	onBack: () -> Unit,
	onSave: () -> Unit,
	content: @Composable () -> Unit
) {
	ModalBottomSheetLayout(
		scrimColor = Color.Unspecified,
		sheetState = state,
		sheetContent = {
			FinancialScreen(
				isScreenVisible = state.isVisible,
				financial = financial,
				financialAction = FinancialAction.EDIT,
				onBack = onBack,
				onSave = onSave
			)
		},
		content = content
	)
}
