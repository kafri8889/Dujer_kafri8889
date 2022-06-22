package com.anafthdev.dujer.uicomponent

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.anafthdev.dujer.data.FinancialGroupData
import com.anafthdev.dujer.data.FinancialGroupDay
import com.anafthdev.dujer.data.FinancialGroupDayItem
import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.util.AppUtil.dateFormatter

fun LazyListScope.swipeableFinancialCard(
	data: FinancialGroupData,
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
		GroupType.DAY -> {
			items(
				items = (data.data as FinancialGroupDay).items,
				key = { item: FinancialGroupDayItem -> item.timeInMillis }
			) { dayItem ->
				Text(
					text = dateFormatter.format(dayItem.timeInMillis),
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
				
				for (financial in dayItem.financials) {
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
