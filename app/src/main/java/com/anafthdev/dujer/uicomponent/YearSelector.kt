package com.anafthdev.dujer.uicomponent

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
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import java.text.SimpleDateFormat

/**
 * year selector
 * @param initialTimeInMillis initial time
 * @param maxYear maximum year, e.g: 2020, 1983, 2037
 * @param minYear minimum year, e.g: 2020, 1983, 2037
 */
@Composable
fun YearSelector(
	initialTimeInMillis: Long,
	modifier: Modifier = Modifier,
	maxYear: String = "",
	minYear: String = "",
	onYearSelected: (Long) -> Unit
) {
	
	val yearFormatter = remember { SimpleDateFormat("yyyy", deviceLocale) }
	var selectedTimeInMillis by rememberSaveable { mutableStateOf(initialTimeInMillis) }
	var previousButtonEnabled by rememberSaveable { mutableStateOf(true) }
	var nextButtonEnabled by rememberSaveable { mutableStateOf(true) }
	
	LaunchedEffect(selectedTimeInMillis) {
		nextButtonEnabled = yearFormatter.format(selectedTimeInMillis) != maxYear
		previousButtonEnabled = yearFormatter.format(selectedTimeInMillis) != minYear
	}
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
	) {
		IconButton(
			enabled = previousButtonEnabled,
			onClick = {
				selectedTimeInMillis -= AppUtil.ONE_YEAR_IN_MILLIS
				onYearSelected(selectedTimeInMillis)
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
			text = yearFormatter.format(selectedTimeInMillis),
			style = MaterialTheme.typography.titleMedium.copy(
				color = LocalUiColor.current.titleText,
				fontSize = MaterialTheme.typography.titleMedium.fontSize.spScaled
			),
			modifier = Modifier
				.padding(4.dpScaled)
		)
		
		IconButton(
			enabled = nextButtonEnabled,
			onClick = {
				selectedTimeInMillis += AppUtil.ONE_YEAR_IN_MILLIS
				onYearSelected(selectedTimeInMillis)
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
