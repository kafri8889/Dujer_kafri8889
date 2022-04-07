package com.anafthdev.dujer.ui.screen.financial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.app.DujerViewModel
import com.anafthdev.dujer.ui.component.TopAppBar
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.black04

@Composable
fun FinancialScreen(
	financialID: Int,
	financialAction: String
) {
	
	val financialViewModel = hiltViewModel<FinancialViewModel>()
	val dujerViewModel = hiltViewModel<DujerViewModel>()
	
	val financial by financialViewModel.financial.observeAsState(initial = Financial.default)
	
	if (financialAction == FinancialViewModel.FINANCIAL_ACTION_EDIT) {
		financialViewModel.getFinancial(financialID)
	}
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.verticalScroll(rememberScrollState())
	) {
		TopAppBar {
			IconButton(
				onClick = {
					dujerViewModel.reset()
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
		}
		
		Text(
			text = stringResource(
				id = if (financialAction == FinancialViewModel.FINANCIAL_ACTION_NEW) R.string._new
				else R.string.edit
			),
			style = Typography.titleLarge.copy(
				fontFamily = Inter
			),
			modifier = Modifier
				.padding(24.dpScaled)
		)
	}
}
