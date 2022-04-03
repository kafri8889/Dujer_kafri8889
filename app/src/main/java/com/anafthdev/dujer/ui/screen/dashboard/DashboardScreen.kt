package com.anafthdev.dujer.ui.screen.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.extension.lastIndexOf
import com.anafthdev.dujer.ui.screen.dashboard.component.BalanceCard
import com.anafthdev.dujer.ui.component.TopAppBar
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.component.FinancialCard
import com.anafthdev.dujer.ui.screen.dashboard.component.ExpenseCard
import com.anafthdev.dujer.ui.screen.dashboard.component.FABNewFinancial
import com.anafthdev.dujer.ui.screen.dashboard.component.IncomeCard
import com.anafthdev.dujer.ui.theme.Inter
import com.anafthdev.dujer.ui.theme.big_shape
import timber.log.Timber
import kotlin.random.Random

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen(
	navController: NavController
) {
	
	val dashboardViewModel = hiltViewModel<DashboardViewModel>()
	
	val userBalance by dashboardViewModel.datastore.getUserBalance.collectAsState(initial = 0.0)
	val currentCurrency by dashboardViewModel.datastore.getCurrentCurrency.collectAsState(initial = Currency.INDONESIAN)
	val mixedFinancialList by dashboardViewModel.mixedFinancialList.observeAsState(initial = emptyList())
	val incomeFinancialList by dashboardViewModel.incomeFinancialList.observeAsState(initial = emptyList())
	val expenseFinancialList by dashboardViewModel.expenseFinancialList.observeAsState(initial = emptyList())
	
	val incomeBalance by rememberUpdatedState(
		newValue = incomeFinancialList.getBy { it.amount }.sum()
	)
	val expenseBalance by rememberUpdatedState(
		newValue = expenseFinancialList.getBy { it.amount }.sum()
	)
	
	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		FABNewFinancial(
			onNewIncome = {
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
			onNewExpense = {
				dashboardViewModel.newRecord(
					Financial(
						id = Random.nextInt(),
						name = "soping",
						amount = 5000.0,
						type = FinancialType.EXPENSE,
						category = Category.shopping,
						currency = currentCurrency,
						dateCreated = System.currentTimeMillis()
					)
				)
			},
			modifier = Modifier
				.padding(32.dpScaled)
				.align(Alignment.BottomEnd)
				.zIndex(2f)
		)
		
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
		) {
			item {
				Column(
					modifier = Modifier
						.padding(horizontal = 16.dpScaled)
						.fillMaxSize()
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
							income = incomeBalance,
							currency = currentCurrency,
							onClick = {
								navController.navigate(
									DujerDestination.IncomeExpense.createRoute(FinancialType.INCOME)
								) {
									launchSingleTop = true
								}
							},
							modifier = Modifier
								.padding(end = 4.dpScaled)
								.weight(1f)
						)
						
						ExpenseCard(
							expense = expenseBalance,
							currency = currentCurrency,
							onClick = {
								navController.navigate(
									DujerDestination.IncomeExpense.createRoute(FinancialType.EXPENSE)
								) {
									launchSingleTop = true
								}
							},
							modifier = Modifier
								.padding(start = 4.dpScaled)
								.weight(1f)
						)
					}
				}
			}
			
			itemsIndexed(
				items = mixedFinancialList,
				key = { _: Int, item: Financial -> item.id }
			) { index, financial ->
				var hasVibrate by remember { mutableStateOf(false) }
				
				val dismissState = rememberDismissState(
					confirmStateChange = {
						if (it == DismissValue.DismissedToStart) {
							dashboardViewModel.deleteRecord(financial)
						} else {
							hasVibrate = false
						}
						
						true
					}
				)
				
				SwipeToDismiss(
					state = dismissState,
					directions = setOf(DismissDirection.EndToStart),
					dismissThresholds = { FractionalThreshold(.6f) },
					background = {
						
						if (
							((2 * dismissState.progress.fraction) >= 1f) and
							(dismissState.targetValue == DismissValue.DismissedToStart) and
							!hasVibrate
						) {
							dashboardViewModel.vibratorManager.vibrate(100)
							hasVibrate = true
						}
						
						Timber.i("swipe fraction: ${(2 * dismissState.progress.fraction)}")
						
						Box(
							modifier = Modifier
								.padding(
									start = 14.dpScaled,
									end = 14.dpScaled,
									top = 8.dpScaled,
									bottom = if (index.lastIndexOf(mixedFinancialList)) 96.dpScaled else 8.dpScaled
								)
								.fillMaxSize()
								.clip(big_shape)
								.background(
									Color(0xFFff4444).copy(
										alpha = (.3f + dismissState.progress.fraction).coerceIn(
											maximumValue = 1f,
											minimumValue = 0f
										)
									)
								)
								.align(Alignment.CenterVertically)
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_trash),
								contentDescription = null,
								modifier = Modifier
									.padding(
										horizontal = 24.dpScaled
									)
									.size(
										if (hasVibrate) 28.dpScaled
										else 28.dpScaled * (2 * dismissState.progress.fraction)
									)
									.align(Alignment.CenterEnd)
							)
						}
					}
				) {
					FinancialCard(
						financial = financial,
						onClick = {
						
						},
						modifier = Modifier
							.padding(
								start = 8.dpScaled,
								end = 8.dpScaled,
								top = 8.dpScaled,
								
								// fab size: 56 dp, fab bottom padding: 24 dp, card to fab padding: 16 dp
								bottom = if (index.lastIndexOf(mixedFinancialList)) 96.dpScaled else 8.dpScaled
							)
							.fillMaxWidth()
					)
				}
			}
		}
	}
}
