package com.anafthdev.dujer.foundation.uicomponent.charting.tokens.extension

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.anafthdev.dujer.foundation.uicomponent.charting.tokens.ChartingColorSchemeKeyTokens

@Composable
fun ChartingColorSchemeKeyTokens.toColor(): Color {
	return when (this) {
		ChartingColorSchemeKeyTokens.Background -> MaterialTheme.colorScheme.background
		ChartingColorSchemeKeyTokens.Error -> MaterialTheme.colorScheme.error
		ChartingColorSchemeKeyTokens.ErrorContainer -> MaterialTheme.colorScheme.errorContainer
		ChartingColorSchemeKeyTokens.InverseOnSurface -> MaterialTheme.colorScheme.inverseOnSurface
		ChartingColorSchemeKeyTokens.InversePrimary -> MaterialTheme.colorScheme.inversePrimary
		ChartingColorSchemeKeyTokens.InverseSurface -> MaterialTheme.colorScheme.inverseSurface
		ChartingColorSchemeKeyTokens.OnBackground -> MaterialTheme.colorScheme.onBackground
		ChartingColorSchemeKeyTokens.OnError -> MaterialTheme.colorScheme.onError
		ChartingColorSchemeKeyTokens.OnErrorContainer -> MaterialTheme.colorScheme.onErrorContainer
		ChartingColorSchemeKeyTokens.OnPrimary -> MaterialTheme.colorScheme.onPrimary
		ChartingColorSchemeKeyTokens.OnPrimaryContainer -> MaterialTheme.colorScheme.onPrimaryContainer
		ChartingColorSchemeKeyTokens.OnSecondary -> MaterialTheme.colorScheme.onSecondary
		ChartingColorSchemeKeyTokens.OnSecondaryContainer -> MaterialTheme.colorScheme.onSecondaryContainer
		ChartingColorSchemeKeyTokens.OnSurface -> MaterialTheme.colorScheme.onSurface
		ChartingColorSchemeKeyTokens.OnSurfaceVariant -> MaterialTheme.colorScheme.onSurfaceVariant
		ChartingColorSchemeKeyTokens.OnTertiary -> MaterialTheme.colorScheme.onTertiary
		ChartingColorSchemeKeyTokens.OnTertiaryContainer -> MaterialTheme.colorScheme.onTertiaryContainer
		ChartingColorSchemeKeyTokens.Outline -> MaterialTheme.colorScheme.outline
		ChartingColorSchemeKeyTokens.Primary -> MaterialTheme.colorScheme.primary
		ChartingColorSchemeKeyTokens.PrimaryContainer -> MaterialTheme.colorScheme.primaryContainer
		ChartingColorSchemeKeyTokens.Secondary -> MaterialTheme.colorScheme.secondary
		ChartingColorSchemeKeyTokens.SecondaryContainer -> MaterialTheme.colorScheme.secondaryContainer
		ChartingColorSchemeKeyTokens.Surface -> MaterialTheme.colorScheme.surface
		ChartingColorSchemeKeyTokens.SurfaceTint -> MaterialTheme.colorScheme.surfaceTint
		ChartingColorSchemeKeyTokens.SurfaceVariant -> MaterialTheme.colorScheme.surfaceVariant
		ChartingColorSchemeKeyTokens.Tertiary -> MaterialTheme.colorScheme.tertiary
		ChartingColorSchemeKeyTokens.TertiaryContainer -> MaterialTheme.colorScheme.tertiaryContainer
	}
}
