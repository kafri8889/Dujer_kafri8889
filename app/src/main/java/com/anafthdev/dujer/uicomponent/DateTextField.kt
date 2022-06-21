package com.anafthdev.dujer.uicomponent

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.foundation.common.DateFormatVisualTransformation
import com.anafthdev.dujer.foundation.common.TextFieldDateFormatter
import com.anafthdev.dujer.foundation.extension.takeDigitString

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DateTextField(
	value: TextFieldValue,
	modifier: Modifier = Modifier,
	trailingIcon: @Composable (() -> Unit)? = null,
	onValueChange: (TextFieldValue) -> Unit
) {
	
	var isBackspacePressed by remember { mutableStateOf(false) }
	
	OutlinedTextField(
		value = value,
		trailingIcon = trailingIcon,
		visualTransformation = DateFormatVisualTransformation(LocalTextStyle.current),
		keyboardOptions = KeyboardOptions(
			keyboardType = KeyboardType.Number,
			imeAction = ImeAction.Done
		),
		onValueChange = {
			// ex: "01-1M-yyyy" -> "011"
			val date = it.text.takeDigitString()
			
			if (date.length < 9) {
				
				// if date length is 3 or 5, move cursor to index + 1
				// ex: (| = cursor) "01-|1M-yyyy" to "01-1|M-yyyy"
				val selection = if (!isBackspacePressed) {
					when (date.length) {
						3, 5 -> it.selection.start + 1
						else -> it.selection.start
					}
				} else it.selection.start
				
				onValueChange(
					it.copy(
						text = TextFieldDateFormatter.format(it),
						selection = TextRange(selection)
					)
				)
			}
		},
		modifier = modifier
			.onKeyEvent { event ->
				if (event.key == Key.Backspace) {
					isBackspacePressed = true
					return@onKeyEvent true
				} else {
					isBackspacePressed = false
				}
				
				false
			}
	)
}
