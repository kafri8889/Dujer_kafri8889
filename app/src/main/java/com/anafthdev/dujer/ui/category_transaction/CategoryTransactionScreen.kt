package com.anafthdev.dujer.ui.category_transaction

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.percentOf
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.uicomponent.DashedDivider
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.TopAppBar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryTransactionScreen(
	categoryID: Int,
	navController: NavController,
	onDeleteTransaction: (Financial) -> Unit
) {
	
	val dujerState = LocalDujerState.current
	
	val viewModel = hiltViewModel<CategoryTransactionViewModel>()
	val state by viewModel.state.collectAsState()
	
	val category = state.category
	
	val allIncomeTransaction = dujerState.allIncomeTransaction
	val allExpenseTransaction = dujerState.allExpenseTransaction
	
	val totalIncomeAmount = remember(allIncomeTransaction) {
		allIncomeTransaction.sumOf { it.amount }
	}
	
	val totalExpenseAmount = remember(allExpenseTransaction) {
		allExpenseTransaction.sumOf { it.amount }
	}
	
	val financialList = remember(allIncomeTransaction, allExpenseTransaction, category.id) {
		(if (category.type == FinancialType.INCOME) allIncomeTransaction else allExpenseTransaction)
			.filter { it.category.id == category.id }
	}
	
	val totalAmount = remember(financialList) {
		financialList.sumOf { it.amount }
	}
	
	val percent = remember(financialList, totalIncomeAmount, totalExpenseAmount) {
		viewModel.percentDecimalFormat.format(
			totalAmount.percentOf(
					value = if (category.type == FinancialType.INCOME) totalIncomeAmount
					else totalExpenseAmount,
					ifNaN = { 0.0 }
				)
		)
	}
	
	LaunchedEffect(categoryID) {
		if (categoryID != Category.default.id) {
			viewModel.dispatch(
				CategoryTransactionAction.GetCategory(categoryID)
			)
		}
	}
	
	BackHandler {
		navController.popBackStack()
	}
	
	LazyColumn(
		modifier = Modifier
			.systemBarsPadding()
			.fillMaxSize()
	) {
		item {
			Column {
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
						text = stringResource(id = R.string.transaction),
						style = Typography.titleLarge.copy(
							color = LocalUiColor.current.headlineText,
							fontWeight = FontWeight.Bold,
							fontSize = Typography.titleLarge.fontSize.spScaled
						)
					)
				}
				
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
						.padding(
							16.dpScaled
						)
				) {
					Icon(
						painter = painterResource(id = category.iconID),
						contentDescription = null,
						tint = category.tint.iconTint.toColor(),
						modifier = Modifier
							.padding(8.dpScaled)
							.size(32.dpScaled)
					)
					
					Column(
						verticalArrangement = Arrangement.Center,
						modifier = Modifier
							.fillMaxWidth()
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.fillMaxWidth()
						) {
							Text(
								maxLines = 2,
								text = category.name,
								overflow = TextOverflow.Ellipsis,
								style = Typography.titleSmall.copy(
									color = LocalUiColor.current.titleText,
									fontWeight = FontWeight.Medium,
									fontSize = Typography.titleSmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.45f)
							)
							
							Text(
								maxLines = 1,
								textAlign = TextAlign.End,
								text = CurrencyFormatter.format(
									locale = deviceLocale,
									amount = totalAmount,
									currencyCode = LocalCurrency.current.countryCode
								),
								overflow = TextOverflow.Ellipsis,
								style = Typography.titleSmall.copy(
									color = LocalUiColor.current.titleText,
									fontWeight = FontWeight.SemiBold,
									fontSize = Typography.titleSmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.55f)
									.horizontalScroll(
										state = rememberScrollState(),
										autoRestart = true
									)
							)
						}
						
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.padding(top = 4.dpScaled)
								.fillMaxWidth()
						) {
							Text(
								maxLines = 1,
								text = "$percent%",
								overflow = TextOverflow.Ellipsis,
								style = Typography.bodySmall.copy(
									color = LocalUiColor.current.bodyText,
									fontSize = Typography.bodySmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.4f)
							)
							
							Text(
								maxLines = 2,
								text = stringResource(
									id = R.string.n_transaction,
									financialList.size.toString()
								),
								textAlign = TextAlign.End,
								overflow = TextOverflow.Ellipsis,
								style = Typography.bodySmall.copy(
									color = LocalUiColor.current.bodyText,
									fontSize = Typography.bodySmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.6f)
							)
						}
					}
				}
				
				DashedDivider(
					thickness = 1.dpScaled,
					phase = 32f,
					intervals = floatArrayOf(16f, 16f),
					modifier = Modifier
						.fillMaxWidth()
				)
				
				Spacer(modifier = Modifier.height(8.dpScaled))
			}
		}
		
		items(
			items = financialList,
			key = { item: Financial -> item.hashCode() }
		) { financial ->
			SwipeableFinancialCard(
				financial = financial,
				onDismissToEnd = { onDeleteTransaction(financial) },
				onClick = {
					navController.navigate(
						DujerDestination.BottomSheet.Financial.createRoute(
							action = FinancialAction.EDIT,
							financialID = financial.id
						)
					)
				},
				modifier = Modifier
					.padding(horizontal = 12.dpScaled)
					.testTag("SwipeableFinancialCard")
			)
		}
	}
}
