package com.anafthdev.dujer.uicomponent.charting.tokens.extension

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.anafthdev.dujer.uicomponent.charting.tokens.ChartingTypographyTokens

@Composable
fun ChartingTypographyTokens.toTextStyle(): TextStyle {
	return when (this) {
		ChartingTypographyTokens.LabelSmall -> MaterialTheme.typography.labelSmall
		ChartingTypographyTokens.LabelMedium -> MaterialTheme.typography.labelMedium
		ChartingTypographyTokens.LabelLarge -> MaterialTheme.typography.labelLarge
		ChartingTypographyTokens.BodySmall -> MaterialTheme.typography.bodySmall
		ChartingTypographyTokens.BodyMedium -> MaterialTheme.typography.bodyMedium
		ChartingTypographyTokens.BodyLarge -> MaterialTheme.typography.bodyLarge
		ChartingTypographyTokens.TitleSmall -> MaterialTheme.typography.titleSmall
		ChartingTypographyTokens.TitleMedium -> MaterialTheme.typography.titleMedium
		ChartingTypographyTokens.TitleLarge -> MaterialTheme.typography.titleLarge
		ChartingTypographyTokens.HeadlineSmall -> MaterialTheme.typography.headlineSmall
		ChartingTypographyTokens.HeadlineMedium -> MaterialTheme.typography.headlineMedium
		ChartingTypographyTokens.HeadlineLarge -> MaterialTheme.typography.headlineLarge
		ChartingTypographyTokens.DisplaySmall -> MaterialTheme.typography.displaySmall
		ChartingTypographyTokens.DisplayMedium -> MaterialTheme.typography.displayMedium
		ChartingTypographyTokens.DisplayLarge -> MaterialTheme.typography.displayLarge
	}
}
