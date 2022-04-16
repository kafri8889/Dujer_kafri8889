package com.anafthdev.dujer.uicomponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.anafthdev.dujer.foundation.window.dpScaled

@Composable
fun TopAppBar(
	modifier: Modifier = Modifier,
	content: @Composable BoxScope.() -> Unit
) {
	Box(
		contentAlignment = Alignment.Center,
		content = content,
		modifier = modifier
			.fillMaxWidth()
			.height(56.dpScaled)
	)
}
