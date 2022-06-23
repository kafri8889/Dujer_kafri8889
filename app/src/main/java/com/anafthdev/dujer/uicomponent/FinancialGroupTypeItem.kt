package com.anafthdev.dujer.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.anafthdev.dujer.data.*
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.AppUtil.dateFormatter

/**
 * @param data data
 * @param onNavigateCategoryClicked invoke when navigate icon clicked [GroupType.CATEGORY]
 */
fun LazyListScope.swipeableFinancialCard(
	data: FinancialGroupData,
	onNavigateCategoryClicked: (Category) -> Unit,
	onFinancialCardCanDelete: () -> Unit,
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
					onCanDelete = onFinancialCardCanDelete,
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
				Text(
					text = formatter.format(item.timeInMillis),
					style = MaterialTheme.typography.titleMedium.copy(
						color = LocalUiColor.current.normalText,
						fontWeight = FontWeight.SemiBold,
						fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
					),
					modifier = Modifier
						.padding(
							top = 8.dpScaled,
							start = 16.dpScaled
						)
				)
				
				for (financial in item.financials) {
					key(financial.hashCode()) {
						SwipeableFinancialCard(
							financial = financial,
							onCanDelete = onFinancialCardCanDelete,
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
							onCanDelete = onFinancialCardCanDelete,
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
