package com.anafthdev.dujer.foundation.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

class DateFormatVisualTransformation(
	private val textStyle: TextStyle
): VisualTransformation {
	
	override fun filter(text: AnnotatedString): TransformedText {
		return TransformedText(
			text = buildAnnotatedString {
				for (word in text) {
					withStyle(
						textStyle.copy(
							color = if (word.isDigit() || word == '-') textStyle.color
							else Color.Gray
						).toSpanStyle()
					) {
						append(word)
					}
				}
			},
			offsetMapping = OffsetMapping.Identity
		)
	}
}