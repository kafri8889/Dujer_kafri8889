package com.anafthdev.dujer.feature.statistic.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowLeft
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import java.text.SimpleDateFormat

@Composable
fun MonthYearSelector(
	modifier: Modifier = Modifier,
	minDate: Long = 0,
	maxDate: Long = System.currentTimeMillis(),
	initialTimeInMillis: Long = System.currentTimeMillis(),
	onDateChanged: (Long) -> Unit
) {
	
	val monthYearFormatter = remember { SimpleDateFormat("MMM yyyy", deviceLocale) }
	var selectedTimeInMillis by rememberSaveable { mutableStateOf(initialTimeInMillis) }
	var previousButtonEnabled by rememberSaveable { mutableStateOf(true) }
	var nextButtonEnabled by rememberSaveable { mutableStateOf(true) }
	
	LaunchedEffect(selectedTimeInMillis) {
		nextButtonEnabled = monthYearFormatter.format(
			selectedTimeInMillis
		) != monthYearFormatter.format(maxDate)
		
		previousButtonEnabled = monthYearFormatter.format(
			selectedTimeInMillis
		) != monthYearFormatter.format(minDate)
	}
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
	) {
		IconButton(
			enabled = previousButtonEnabled,
			onClick = {
				selectedTimeInMillis -= AppUtil.ONE_MONTH_IN_MILLIS
				onDateChanged(selectedTimeInMillis)
			}
		) {
			Icon(
				imageVector = Icons.Rounded.ArrowLeft,
				contentDescription = null,
				modifier = Modifier
					.size(28.dpScaled)
			)
		}
		
		Text(
			text = monthYearFormatter.format(selectedTimeInMillis),
			style = MaterialTheme.typography.bodyLarge.copy(
				fontSize = MaterialTheme.typography.bodyLarge.fontSize.spScaled
			),
			modifier = Modifier
				.padding(4.dpScaled)
		)
		
		IconButton(
			enabled = nextButtonEnabled,
			onClick = {
				selectedTimeInMillis += AppUtil.ONE_MONTH_IN_MILLIS
				onDateChanged(selectedTimeInMillis)
			}
		) {
			Icon(
				imageVector = Icons.Rounded.ArrowRight,
				contentDescription = null,
				modifier = Modifier
					.size(28.dpScaled)
			)
		}
	}
	
}
