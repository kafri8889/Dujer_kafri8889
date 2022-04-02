package com.anafthdev.dujer.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialCard(
	financial: Financial,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	
	Box(
		modifier = modifier
			.background(
				shape = big_shape,
				color = Color(0x80F8F8F8)
			)
	) {
		Card(
			shape = big_shape,
			containerColor = Color.White,
			onClick = onClick,
			modifier = Modifier
				.padding(
					top = 2.dpScaled,
					start = 4.dpScaled,
					end = 4.dpScaled,
					bottom = 8.dpScaled,
				)
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dpScaled)
			) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.size(64.dpScaled)
						.clip(normal_shape)
						.background(financial.category.tint.backgroundTint.toColor())
				) {
					Icon(
						painter = painterResource(id = financial.category.iconID),
						tint = financial.category.tint.iconTint.toColor(),
						contentDescription = null
					)
				}
				
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.padding(start = 8.dpScaled)
						.fillMaxSize()
						.height(64.dpScaled)
				) {
					Column(
						modifier = Modifier
							.weight(0.4f)
					) {
						Text(
							text = financial.name,
							style = Typography.bodyMedium.copy(
								fontFamily = Inter,
								fontWeight = FontWeight.SemiBold,
								fontSize = Typography.bodyMedium.fontSize.spScaled
							)
						)
						
						Text(
							text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(financial.dateCreated),
							style = Typography.labelSmall.copy(
								fontFamily = Inter,
								fontWeight = FontWeight.Normal,
								fontSize = Typography.labelSmall.fontSize.spScaled
							),
							modifier = Modifier
								.padding(top = 8.dpScaled)
						)
					}
					
					Column(
						horizontalAlignment = Alignment.End,
						modifier = Modifier
							.weight(0.6f)
					) {
						Text(
							text = "${if (financial.type == FinancialType.INCOME) "+" else "-"}${financial.currency.symbol} ${financial.amount}",
							style = Typography.bodyMedium.copy(
								fontFamily = Inter,
								fontWeight = FontWeight.SemiBold,
								fontSize = Typography.bodyMedium.fontSize.spScaled
							)
						)
						
						Text(
							text = financial.type.name,
							style = Typography.labelSmall.copy(
								color = Color(0xFF4A5568),
								fontFamily = Inter,
								fontWeight = FontWeight.Medium,
								fontSize = Typography.labelSmall.fontSize.spScaled
							),
							modifier = Modifier
								.padding(top = 8.dpScaled)
						)
					}
				}
			}
		}
	}
}
