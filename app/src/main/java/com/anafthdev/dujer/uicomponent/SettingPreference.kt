package com.anafthdev.dujer.uicomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.SettingPreference
import com.anafthdev.dujer.ui.theme.Typography

@OptIn(ExperimentalUnitApi::class)
@Composable
fun SettingPreference(
	preference: SettingPreference,
	onClick: (Any?) -> Unit
) {
	when (preference.type) {
		SettingPreference.PreferenceType.BASIC -> {
			BasicPreference(
				preference = preference,
				onClick = onClick
			)
		}
		
		SettingPreference.PreferenceType.SWITCH -> {
			SwitchPreference(
				preference = preference,
				onClick = onClick
			)
		}
		SettingPreference.PreferenceType.CUSTOM -> {
		
		}
	}
}

@Composable
fun SettingPreferences(
	preferences: List<SettingPreference>,
	modifier: Modifier = Modifier,
	onClick: (SettingPreference) -> Unit
) {
	val groupedPreference = preferences.groupBy { it.category }
	
	groupedPreference.forEach {
		
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.then(modifier)
		) {
			Text(
				text = it.key,
				style = Typography.titleMedium.copy(
					color = MaterialTheme.colorScheme.primary,
					fontWeight = FontWeight.Bold,
					fontSize = Typography.titleMedium.fontSize.spScaled
				),
				modifier = Modifier
					.padding(
						top = 16.dpScaled,
						bottom = 8.dpScaled,
						start = 12.dpScaled,
						end = 12.dpScaled
					)
			)
			
			it.value.forEach { preference ->
				SettingPreference(
					preference = preference,
					onClick = {
						onClick(preference.apply { value = it ?: "" })
					}
				)
			}
		}
	}
}

@Composable
internal fun BasicPreference(
	preference: SettingPreference,
	onClick: (Any?) -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.height(64.dpScaled)
			.clickable { onClick(null) }
	) {
		if (preference.iconResId != null) {
			Icon(
				painter = painterResource(id = preference.iconResId),
				contentDescription = null,
				modifier = Modifier
					.size(24.dpScaled)
					.weight(
						weight = 0.12f
					)
			)
		}
		
		Column(
			verticalArrangement = Arrangement.Center,
			modifier = Modifier
				.weight(
					weight = if (preference.showValue) 0.6f else 0.88f
				)
		) {
			Text(
				text = preference.title,
				style = Typography.titleMedium.copy(
					fontWeight = FontWeight.Medium,
					fontSize = Typography.titleMedium.fontSize.spScaled
				),
				modifier = Modifier
					.padding(
						start = if (preference.iconResId != null) 0.dpScaled else 12.dpScaled
					)
			)
			
			if (preference.summary.isNotBlank()) {
				Text(
					text = preference.summary,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					style = Typography.titleSmall.copy(
						color = Color.Gray,
						fontWeight = FontWeight.Normal,
						fontSize = Typography.titleSmall.fontSize.spScaled
					),
					modifier = Modifier
						.padding(
							start = if (preference.iconResId != null) 0.dpScaled else 12.dpScaled
						)
				)
			}
		}
		
		if (preference.showValue) {
			Box(
				contentAlignment = Alignment.CenterEnd,
				modifier = Modifier
					.padding(end = 12.dpScaled)
					.weight(0.28f)
			) {
				Text(
					text = preference.value.toString(),
					textAlign = TextAlign.End,
					style = Typography.titleMedium.copy(
						fontSize = 15.spScaled,
						fontWeight = FontWeight.Normal,
						color = MaterialTheme.colorScheme.primary
					)
				)
			}
		}
	}
}

@Composable
internal fun SwitchPreference(
	preference: SettingPreference,
	onClick: (Any?) -> Unit
) {
	
	val isChecked by rememberUpdatedState(newValue = preference.value as Boolean)
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.height(64.dpScaled)
			.clickable {
				onClick(!isChecked)
			}
	) {
		if (preference.iconResId != null) {
			Icon(
				painter = painterResource(id = preference.iconResId),
				contentDescription = null,
				modifier = Modifier
					.size(24.dpScaled)
					.weight(0.12f)
			)
		}
		
		Column(
			verticalArrangement = Arrangement.Center,
			modifier = Modifier
				.weight(0.6f)
		) {
			Text(
				text = preference.title,
				style = Typography.titleMedium.copy(
					fontWeight = FontWeight.Medium,
					fontSize = Typography.titleMedium.fontSize.spScaled
				),
				modifier = Modifier
					.padding(
						start = if (preference.iconResId != null) 0.dpScaled else 12.dpScaled
					)
			)
			
			if (preference.summary.isNotBlank()) {
				Text(
					text = preference.summary,
					style = Typography.titleSmall.copy(
						color = Color.Gray,
						fontWeight = FontWeight.Normal,
						fontSize = Typography.titleSmall.fontSize.spScaled
					),
					modifier = Modifier
						.padding(
							start = if (preference.iconResId != null) 0.dpScaled else 12.dpScaled
						)
				)
			}
		}
		
		Switch(
			checked = isChecked,
			onCheckedChange = {
				onClick(!isChecked)
			},
			modifier = Modifier
				.weight(0.28f)
		)
	}
}
