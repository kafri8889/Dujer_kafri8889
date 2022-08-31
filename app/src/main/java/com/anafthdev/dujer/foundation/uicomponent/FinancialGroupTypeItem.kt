package com.anafthdev.dujer.foundation.uicomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.*
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.common.AppUtil.dateFormatter
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency

/**
 * @param data data
 * @param onNavigateCategoryClicked invoke when navigate icon clicked [GroupType.CATEGORY]
 */
fun LazyListScope.swipeableFinancialCard(
	data: FinancialGroupData,
	onNavigateCategoryClicked: (Category) -> Unit,
	onFinancialCardDismissToEnd: (Financial) -> Unit,
	onFinancialCardClicked: (Financial) -> Unit,
) {
	
	when (data.type) {
		GroupType.DEFAULT -> {
			items(
				items = data.data.rawFinancials,
				key = { item: Financial -> item.hashCode() }
			) { financial ->
				SwipeableFinancialCard(
					financial = financial,
					onDismissToEnd = { onFinancialCardDismissToEnd(financial) },
					onClick = { onFinancialCardClicked(financial) },
					modifier = Modifier
						.padding(horizontal = 12.dpScaled)
						.testTag("SwipeableFinancialCard")
				)
			}
		}
		GroupType.DAY, GroupType.MONTH, GroupType.YEAR -> {
			val formatter = when (data.type) {
				GroupType.DAY -> dateFormatter
				GroupType.MONTH -> AppUtil.monthYearFormatter
				GroupType.YEAR -> AppUtil.yearFormatter
				else -> throw RuntimeException("no formatter set")
			}
			
			items(
				items = (data.data as FinancialGroupDate).items,
				key = { item: FinancialGroupDateItem -> item.timeInMillis }
			) { item ->
				
				var isGroupInfoShowed by remember { mutableStateOf(false) }
				
				val incomeAmount = remember(item.financials) {
					item.financials
						.filter { it.type == FinancialType.INCOME }
						.sumOf { it.amount }
				}
				
				val expenseAmount = remember(item.financials) {
					item.financials
						.filter { it.type == FinancialType.EXPENSE }
						.sumOf { it.amount }
				}
				
				val iconRotationDegree by animateFloatAsState(
					targetValue = if (isGroupInfoShowed) 180f else 360f
				)
				
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.End,
					modifier = Modifier
						.padding(
							top = 8.dpScaled,
							start = 16.dpScaled,
							end = 16.dpScaled
						)
						.fillMaxWidth()
				) {
					Text(
						text = formatter.format(item.timeInMillis),
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.normalText,
							fontWeight = FontWeight.SemiBold,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						)
					)
					
					Spacer(modifier = Modifier.weight(1f))
					
					IconButton(
						onClick = {
							isGroupInfoShowed = !isGroupInfoShowed
						},
						modifier = Modifier
							.rotate(iconRotationDegree)
					) {
						Icon(
							imageVector = Icons.Rounded.ArrowDropDown,
							contentDescription = null
						)
					}
				}
				
				AnimatedVisibility(
					visible = isGroupInfoShowed,
					enter = expandVertically(),
					exit = shrinkVertically(
						animationSpec = tween(400)
					),
					modifier = Modifier
						.padding(horizontal = 8.dpScaled)
				) {
					Column {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.padding(
									top = 8.dpScaled,
									start = 16.dpScaled,
									end = 16.dpScaled
								)
								.fillMaxWidth()
						) {
							Text(
								text = stringResource(id = R.string.transaction),
								style = MaterialTheme.typography.titleSmall.copy(
									color = LocalUiColor.current.normalText,
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
								)
							)
							
							Spacer(modifier = Modifier.weight(1f))
							
							Text(
								text = item.financials.size.toString(),
								style = MaterialTheme.typography.bodyMedium.copy(
									color = LocalUiColor.current.bodyText,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								)
							)
						}
						
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.padding(
									top = 8.dpScaled,
									start = 16.dpScaled,
									end = 16.dpScaled
								)
								.fillMaxWidth()
						) {
							Text(
								text = stringResource(id = R.string.income),
								style = MaterialTheme.typography.titleSmall.copy(
									color = LocalUiColor.current.normalText,
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.35f)
							)
							
							Spacer(modifier = Modifier.weight(0.1f))
							
							Text(
								textAlign = TextAlign.End,
								text = CurrencyFormatter.format(
									locale = deviceLocale,
									amount = incomeAmount,
									currencyCode = LocalCurrency.current.countryCode
								),
								style = MaterialTheme.typography.bodyMedium.copy(
									color = LocalUiColor.current.bodyText,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
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
								.padding(
									vertical = 8.dpScaled,
									horizontal = 16.dpScaled
								)
								.fillMaxWidth()
						) {
							Text(
								text = stringResource(id = R.string.expenses),
								style = MaterialTheme.typography.titleSmall.copy(
									color = LocalUiColor.current.normalText,
									fontWeight = FontWeight.Medium,
									fontSize = MaterialTheme.typography.titleSmall.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.35f)
							)
							
							Spacer(modifier = Modifier.weight(0.1f))
							
							Text(
								textAlign = TextAlign.End,
								text = CurrencyFormatter.format(
									locale = deviceLocale,
									amount = expenseAmount,
									currencyCode = LocalCurrency.current.countryCode
								),
								style = MaterialTheme.typography.bodyMedium.copy(
									color = LocalUiColor.current.bodyText,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize.spScaled
								),
								modifier = Modifier
									.weight(0.55f)
									.horizontalScroll(
										state = rememberScrollState(),
										autoRestart = true
									)
							)
						}
					}
				}
				
				for (financial in item.financials) {
					key(financial.hashCode()) {
						SwipeableFinancialCard(
							financial = financial,
							onDismissToEnd = { onFinancialCardDismissToEnd(financial) },
							onClick = { onFinancialCardClicked(financial) },
							modifier = Modifier
								.padding(horizontal = 12.dpScaled)
								.testTag("SwipeableFinancialCard")
						)
					}
				}
			}
		}
		GroupType.CATEGORY -> {
			items(
				items = (data.data as FinancialGroupCategory).items,
				key = { item: FinancialGroupCategoryItem -> item.category.id }
			) { item ->
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.End,
					modifier = Modifier
						.padding(
							top = 8.dpScaled,
							start = 16.dpScaled,
							end = 16.dpScaled
						)
						.fillMaxWidth()
				) {
					Text(
						text = item.category.name,
						textAlign = TextAlign.Start,
						style = MaterialTheme.typography.titleMedium.copy(
							color = LocalUiColor.current.normalText,
							fontWeight = FontWeight.SemiBold,
							fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
						),
						modifier = Modifier
							.weight(0.84f)
					)
					
					IconButton(
						onClick = {
							onNavigateCategoryClicked(item.category)
						},
						modifier = Modifier
							.weight(0.16f)
					) {
						Icon(
							imageVector = Icons.Rounded.NavigateNext,
							contentDescription = null
						)
					}
				}
				
				for (financial in item.financials) {
					key(financial.hashCode()) {
						SwipeableFinancialCard(
							financial = financial,
							onDismissToEnd = { onFinancialCardDismissToEnd(financial) },
							onClick = { onFinancialCardClicked(financial) },
							modifier = Modifier
								.padding(horizontal = 12.dpScaled)
								.testTag("SwipeableFinancialCard")
						)
					}
				}
			}
		}
	}
	

}
