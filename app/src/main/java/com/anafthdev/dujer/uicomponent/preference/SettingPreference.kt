package com.anafthdev.dujer.uicomponent.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.Typography

@Composable
fun SettingPreference(
	preference: Preference,
	onClick: (Any) -> Unit
) {
	when (preference) {
		is BasicPreference -> {
			BasicPreference(
				preference = preference,
				onClick = onClick
			)
		}
		
		is SwitchPreference -> {
			SwitchPreference(
				preference = preference,
				onClick = onClick
			)
		}
	}
}

@Composable
fun SettingPreferences(
	preferences: List<Preference>,
	modifier: Modifier = Modifier,
	onClick: (Preference) -> Unit
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
						onClick(
							when (preference) {
								is BasicPreference -> preference.copy(value = it)
								is SwitchPreference -> preference.copy(isChecked = it as Boolean)
							}
						)
					}
				)
			}
		}
	}
}

@Composable
internal fun BasicPreference(
	preference: BasicPreference,
	onClick: (Any) -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.height(64.dpScaled)
			.clickable { onClick("") }
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
		} else {
			Box(
				modifier = Modifier
					.weight(weight = 0.12f)
			)
		}
		
		Column(
			verticalArrangement = Arrangement.Center,
			modifier = Modifier
				.weight(
					weight = 0.68f
				)
		) {
			Text(
				text = preference.title,
				style = Typography.titleMedium.copy(
					fontWeight = FontWeight.Medium,
					fontSize = Typography.titleMedium.fontSize.spScaled
				)
			)
			
			if (preference.summary.isNotBlank()) {
				Text(
					text = preference.summary,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					style = Typography.titleSmall.copy(
						color = Color.Gray,
						fontWeight = FontWeight.Normal,
						fontSize = Typography.titleSmall.fontSize.spScaled
					)
				)
			}
		}
		
		Box(
			contentAlignment = Alignment.CenterEnd,
			modifier = Modifier
				.padding(end = 12.dpScaled)
				.weight(0.2f)
		) {
			if (preference.showValue) {
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
	preference: SwitchPreference,
	onClick: (Any) -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.height(64.dpScaled)
			.clickable {
				onClick(!preference.isChecked)
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
		} else {
			Box(
				modifier = Modifier
					.weight(weight = 0.12f)
			)
		}
		
		Column(
			verticalArrangement = Arrangement.Center,
			modifier = Modifier
				.weight(0.68f)
		) {
			Text(
				text = preference.title,
				style = Typography.titleMedium.copy(
					fontWeight = FontWeight.Medium,
					fontSize = Typography.titleMedium.fontSize.spScaled
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
					)
				)
			}
		}
		
		Switch(
			checked = preference.isChecked,
			onCheckedChange = {
				onClick(!preference.isChecked)
			},
			modifier = Modifier
				.weight(0.2f)
		)
	}
}

sealed interface Preference {
	val title: String
	val summary: String
	val category: String
	val iconResId: Int?
}

data class BasicPreference(
	override val title: String,
	override val summary: String,
	override val category: String = "",
	override val iconResId: Int? = null,
	var value: Any = "",
	var showValue: Boolean = false
): Preference

data class SwitchPreference(
	override val title: String,
	override val summary: String,
	override val category: String = "",
	override val iconResId: Int? = null,
	var isChecked: Boolean,
): Preference
