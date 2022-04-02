package com.anafthdev.dujer.ui.screen.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.lastIndexOf
import com.anafthdev.dujer.ui.screen.dashboard.component.BalanceCard
import com.anafthdev.dujer.ui.component.TopAppBar
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.component.FinancialCard
import com.anafthdev.dujer.ui.screen.dashboard.component.ExpenseCard
import com.anafthdev.dujer.ui.screen.dashboard.component.IncomeCard
import com.anafthdev.dujer.ui.theme.Inter
import kotlin.random.Random

@Composable
fun DashboardScreen(
	navController: NavController
) {
	
	val dashboardViewModel = hiltViewModel<DashboardViewModel>()
	
	val userBalance by dashboardViewModel.datastore.getUserBalance.collectAsState(initial = 0.0)
	val currentCurrency by dashboardViewModel.datastore.getCurrentCurrency.collectAsState(initial = Currency.INDONESIAN)
	val mixedFinancialList by dashboardViewModel.mixedFinancialList.observeAsState(initial = emptyList())
	
	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		FloatingActionButton(
			onClick = {
				dashboardViewModel.newRecord(
					Financial(
						id = Random.nextInt(),
						name = "tes",
						amount = 5000.0,
						type = FinancialType.INCOME,
						category = Category.food,
						currency = currentCurrency,
						dateCreated = System.currentTimeMillis()
					)
				)
			},
			modifier = Modifier
				.padding(32.dpScaled)
				.align(Alignment.BottomEnd)
				.zIndex(2f)
		) {
			Icon(
				imageVector = Icons.Rounded.Add,
				contentDescription = null
			)
		}
		
		Column(
			modifier = Modifier
				.padding(horizontal = 16.dpScaled)
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
		) {
			TopAppBar {
				Text(
					text = stringResource(id = R.string.dashboard),
					style = Typography.titleLarge.copy(
						fontFamily = Inter,
						fontWeight = FontWeight.Bold,
						fontSize = Typography.titleLarge.fontSize.spScaled
					)
				)
			}
			
			BalanceCard(
				balance = userBalance,
				currency = currentCurrency
			)
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						vertical = 16.dpScaled
					)
			) {
				IncomeCard(
					income = 0.0,
					currency = currentCurrency,
					onClick = {
						navController.navigate(DujerDestination.Income.route) {
							launchSingleTop = true
						}
					},
					modifier = Modifier
						.padding(end = 4.dpScaled)
						.weight(1f)
				)
				
				ExpenseCard(
					expense = 0.0,
					currency = currentCurrency,
					onClick = {
					
					},
					modifier = Modifier
						.padding(start = 4.dpScaled)
						.weight(1f)
				)
			}
			
			mixedFinancialList.forEachIndexed { index, financial ->
				FinancialCard(
					financial = financial,
					onClick = {
					
					},
					modifier = Modifier
						.padding(
							// fab size: 56 dp, fab bottom padding: 24 dp, card to fab padding: 16 dp
							bottom = if (index.lastIndexOf(mixedFinancialList)) 96.dpScaled else 0.dpScaled
						)
				)
			}
		}
	}
}
