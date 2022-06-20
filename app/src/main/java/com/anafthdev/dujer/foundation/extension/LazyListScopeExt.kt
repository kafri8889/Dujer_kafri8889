package com.anafthdev.dujer.foundation.extension

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier

fun <T> LazyListScope.gridItems(
	items: Array<T>,
	nColumns: Int,
	horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
	key: ((item: T) -> Any)? = null,
	itemContent: @Composable BoxScope.(T) -> Unit,
) {
	gridItems(
		items = items.toList(),
		nColumns = nColumns,
		horizontalArrangement = horizontalArrangement,
		key = key,
		itemContent = itemContent
	)
}

fun <T> LazyListScope.gridItems(
	items: List<T>,
	nColumns: Int,
	horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
	key: ((item: T) -> Any)? = null,
	itemContent: @Composable BoxScope.(T) -> Unit,
) {
	val rows = if (items.isEmpty()) 0 else 1 + (items.count() - 1) / nColumns
	items(rows) { rowIndex ->
		Row(horizontalArrangement = horizontalArrangement) {
			for (columnIndex in 0 until nColumns) {
				val itemIndex = rowIndex * nColumns + columnIndex
				if (itemIndex < items.count()) {
					val item = items[itemIndex]
					key(key?.invoke(item)) {
						Box(
							modifier = Modifier.weight(1f, fill = true),
							propagateMinConstraints = true
						) {
							itemContent.invoke(this, item)
						}
					}
				} else {
					Spacer(Modifier.weight(1f, fill = true))
				}
			}
		}
	}
}
