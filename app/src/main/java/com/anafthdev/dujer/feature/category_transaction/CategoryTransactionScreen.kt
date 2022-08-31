package com.anafthdev.dujer.feature.category_transaction

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.financial.data.FinancialAction
import com.anafthdev.dujer.feature.theme.Typography
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uicomponent.DashedDivider
import com.anafthdev.dujer.foundation.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.foundation.uicomponent.TopAppBar
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency

@Composable
fun CategoryTransactionScreen(
	navController: NavController,
	viewModel: CategoryTransactionViewModel,
	onDeleteTransaction: (Financial) -> Unit
) {
	
	val state by viewModel.state.collectAsState()
	
	val totalAmountForCurrentCategory = remember(state.category) {
		state.category.financials.sumOf { it.amount }
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
						painter = painterResource(id = state.category.iconID),
						contentDescription = null,
						tint = state.category.tint.iconTint.toColor(),
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
								text = state.category.name,
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
									amount = totalAmountForCurrentCategory,
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
								text = state.percent,
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
									state.category.financials.size.toString()
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
			items = state.category.financials,
			key = { item: Financial -> item.hashCode() }
		) { financial ->
			SwipeableFinancialCard(
				financial = financial,
				onDismissToEnd = { onDeleteTransaction(financial) },
				onClick = {
					navController.navigate(
						DujerDestination.BottomSheet.Financial.Home.createRoute(
							financialAction = FinancialAction.EDIT,
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
