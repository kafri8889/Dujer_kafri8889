package com.anafthdev.dujer.uicomponent

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.anafthdev.dujer.foundation.extension.ColorSchemeKeyTokens
import com.anafthdev.dujer.foundation.extension.ShapeKeyTokens
import com.anafthdev.dujer.foundation.extension.TypographyKeyTokens
import com.anafthdev.dujer.foundation.extension.toShape
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.Typography

@Composable
fun OutlinedTextField(
	value: String,
	maxCounter: Int,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	readOnly: Boolean = false,
	textStyle: TextStyle = LocalTextStyle.current,
	label: @Composable (() -> Unit)? = null,
	placeholder: @Composable (() -> Unit)? = null,
	leadingIcon: @Composable (() -> Unit)? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	isError: Boolean = false,
	visualTransformation: VisualTransformation = VisualTransformation.None,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	singleLine: Boolean = false,
	maxLines: Int = Int.MAX_VALUE,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
	shape: Shape = OutlinedTextFieldTokens.ContainerShape.toShape(),
	colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
	
	val textLength by rememberUpdatedState(newValue = value.length)
	
	Column(
		modifier = modifier
		) {
		OutlinedTextField(
			value = value,
			onValueChange = { s ->
				if (textLength < maxCounter) {
					onValueChange(s)
				}
			},
			modifier = Modifier
				.fillMaxWidth(),
			enabled = enabled,
			readOnly = readOnly,
			textStyle = textStyle,
			label = label,
			placeholder = placeholder,
			leadingIcon = leadingIcon,
			trailingIcon = trailingIcon,
			isError = isError,
			visualTransformation = visualTransformation,
			keyboardOptions = keyboardOptions,
			keyboardActions = keyboardActions,
			singleLine = singleLine,
			maxLines = maxLines,
			interactionSource = interactionSource,
			shape = shape,
			colors = colors
		)
		
		Text(
			text = "$textLength/$maxCounter",
			style = Typography.bodyMedium.copy(
				color = Color.Gray,
				fontSize = Typography.bodyMedium.fontSize.spScaled
			),
			modifier = Modifier
				.padding(top = 8.dpScaled)
				.align(Alignment.End)
		)
	}
}

object OutlinedTextFieldTokens {
	val CaretColor = ColorSchemeKeyTokens.Primary
	val ContainerHeight = 56.0.dp
	val ContainerShape = ShapeKeyTokens.CornerExtraSmall
	val DisabledInputColor = ColorSchemeKeyTokens.OnSurface
	const val DisabledInputOpacity = 0.38f
	val DisabledLabelColor = ColorSchemeKeyTokens.OnSurface
	const val DisabledLabelOpacity = 0.38f
	val DisabledLeadingIconColor = ColorSchemeKeyTokens.OnSurface
	const val DisabledLeadingIconOpacity = 0.38f
	val DisabledOutlineColor = ColorSchemeKeyTokens.OnSurface
	const val DisabledOutlineOpacity = 0.12f
	val DisabledOutlineWidth = 1.0.dp
	val DisabledSupportingColor = ColorSchemeKeyTokens.OnSurface
	const val DisabledSupportingOpacity = 0.38f
	val DisabledTrailingIconColor = ColorSchemeKeyTokens.OnSurface
	const val DisabledTrailingIconOpacity = 0.38f
	val ErrorFocusCaretColor = ColorSchemeKeyTokens.Error
	val ErrorFocusInputColor = ColorSchemeKeyTokens.OnSurface
	val ErrorFocusLabelColor = ColorSchemeKeyTokens.Error
	val ErrorFocusLeadingIconColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val ErrorFocusOutlineColor = ColorSchemeKeyTokens.Error
	val ErrorFocusSupportingColor = ColorSchemeKeyTokens.Error
	val ErrorFocusTrailingIconColor = ColorSchemeKeyTokens.Error
	val ErrorHoverInputColor = ColorSchemeKeyTokens.OnSurface
	val ErrorHoverLabelColor = ColorSchemeKeyTokens.OnErrorContainer
	val ErrorHoverLeadingIconColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val ErrorHoverOutlineColor = ColorSchemeKeyTokens.OnErrorContainer
	val ErrorHoverSupportingColor = ColorSchemeKeyTokens.Error
	val ErrorHoverTrailingIconColor = ColorSchemeKeyTokens.OnErrorContainer
	val ErrorInputColor = ColorSchemeKeyTokens.OnSurface
	val ErrorLabelColor = ColorSchemeKeyTokens.Error
	val ErrorLeadingIconColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val ErrorOutlineColor = ColorSchemeKeyTokens.Error
	val ErrorSupportingColor = ColorSchemeKeyTokens.Error
	val ErrorTrailingIconColor = ColorSchemeKeyTokens.Error
	val FocusInputColor = ColorSchemeKeyTokens.OnSurface
	val FocusLabelColor = ColorSchemeKeyTokens.Primary
	val FocusLeadingIconColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val FocusOutlineColor = ColorSchemeKeyTokens.Primary
	val FocusOutlineWidth = 2.0.dp
	val FocusSupportingColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val FocusTrailingIconColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val HoverInputColor = ColorSchemeKeyTokens.OnSurface
	val HoverLabelColor = ColorSchemeKeyTokens.OnSurface
	val HoverLeadingIconColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val HoverOutlineColor = ColorSchemeKeyTokens.OnSurface
	val HoverOutlineWidth = 1.0.dp
	val HoverSupportingColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val HoverTrailingIconColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val InputColor = ColorSchemeKeyTokens.OnSurface
	val InputFont = TypographyKeyTokens.BodyLarge
	val InputPlaceholderColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val InputPrefixColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val InputSuffixColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val LabelColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val LabelFont = TypographyKeyTokens.BodyLarge
	val LeadingIconColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val LeadingIconSize = 24.0.dp
	val OutlineColor = ColorSchemeKeyTokens.Outline
	val OutlineWidth = 1.0.dp
	val SupportingColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val SupportingFont = TypographyKeyTokens.BodySmall
	val TrailingIconColor = ColorSchemeKeyTokens.OnSurfaceVariant
	val TrailingIconSize = 24.0.dp
}
