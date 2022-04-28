package com.anafthdev.dujer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.anafthdev.dujer.R

val Inter = FontFamily(
	Font(R.font.inter_thin, FontWeight.Thin),
	Font(R.font.inter_extra_light, FontWeight.ExtraLight),
	Font(R.font.inter_light, FontWeight.Light),
	Font(R.font.inter_regular, FontWeight.Normal),
	Font(R.font.inter_medium, FontWeight.Medium),
	Font(R.font.inter_semi_bold, FontWeight.SemiBold),
	Font(R.font.inter_bold, FontWeight.Bold),
	Font(R.font.inter_extra_bold, FontWeight.ExtraBold),
)

val Typography = Typography(
	displayLarge = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.W400,
		fontSize = 57.sp,
		lineHeight = 64.sp,
		letterSpacing = (-0.25).sp,
	),
	displayMedium = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.W400,
		fontSize = 45.sp,
		lineHeight = 52.sp,
		letterSpacing = 0.sp,
	),
	displaySmall = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.W400,
		fontSize = 36.sp,
		lineHeight = 44.sp,
		letterSpacing = 0.sp,
	),
	headlineLarge = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.W400,
		fontSize = 32.sp,
		lineHeight = 40.sp,
		letterSpacing = 0.sp,
	),
	headlineMedium = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.W400,
		fontSize = 28.sp,
		lineHeight = 36.sp,
		letterSpacing = 0.sp,
	),
	headlineSmall = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.W400,
		fontSize = 24.sp,
		lineHeight = 32.sp,
		letterSpacing = 0.sp,
	),
	titleLarge = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.W400,
		fontSize = 22.sp,
		lineHeight = 28.sp,
		letterSpacing = 0.sp,
	),
	titleMedium = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.Medium,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.1.sp,
	),
	titleSmall = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.1.sp,
	),
	labelLarge = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.1.sp,
	),
	bodyLarge = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.W400,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.5.sp,
	),
	bodyMedium = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.W400,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.25.sp,
	),
	bodySmall = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.W400,
		fontSize = 12.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.4.sp,
	),
	labelMedium = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.Medium,
		fontSize = 12.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.5.sp,
	),
	labelSmall = TextStyle(
		fontFamily = Inter,
		fontWeight = FontWeight.Medium,
		fontSize = 11.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.5.sp,
	)
)

object TypographyTokens {
	val BodyLarge = TextStyle(
		fontFamily = TypeScaleTokens.BodyLargeFont,
		fontWeight = TypeScaleTokens.BodyLargeWeight,
		fontSize = TypeScaleTokens.BodyLargeSize,
		lineHeight = TypeScaleTokens.BodyLargeLineHeight,
		letterSpacing = TypeScaleTokens.BodyLargeTracking,
	)
	val BodyMedium = TextStyle(
		fontFamily = TypeScaleTokens.BodyMediumFont,
		fontWeight = TypeScaleTokens.BodyMediumWeight,
		fontSize = TypeScaleTokens.BodyMediumSize,
		lineHeight = TypeScaleTokens.BodyMediumLineHeight,
		letterSpacing = TypeScaleTokens.BodyMediumTracking,
	)
	val BodySmall = TextStyle(
		fontFamily = TypeScaleTokens.BodySmallFont,
		fontWeight = TypeScaleTokens.BodySmallWeight,
		fontSize = TypeScaleTokens.BodySmallSize,
		lineHeight = TypeScaleTokens.BodySmallLineHeight,
		letterSpacing = TypeScaleTokens.BodySmallTracking,
	)
	val DisplayLarge = TextStyle(
		fontFamily = TypeScaleTokens.DisplayLargeFont,
		fontWeight = TypeScaleTokens.DisplayLargeWeight,
		fontSize = TypeScaleTokens.DisplayLargeSize,
		lineHeight = TypeScaleTokens.DisplayLargeLineHeight,
		letterSpacing = TypeScaleTokens.DisplayLargeTracking,
	)
	val DisplayMedium = TextStyle(
		fontFamily = TypeScaleTokens.DisplayMediumFont,
		fontWeight = TypeScaleTokens.DisplayMediumWeight,
		fontSize = TypeScaleTokens.DisplayMediumSize,
		lineHeight = TypeScaleTokens.DisplayMediumLineHeight,
		letterSpacing = TypeScaleTokens.DisplayMediumTracking,
	)
	val DisplaySmall = TextStyle(
		fontFamily = TypeScaleTokens.DisplaySmallFont,
		fontWeight = TypeScaleTokens.DisplaySmallWeight,
		fontSize = TypeScaleTokens.DisplaySmallSize,
		lineHeight = TypeScaleTokens.DisplaySmallLineHeight,
		letterSpacing = TypeScaleTokens.DisplaySmallTracking,
	)
	val HeadlineLarge = TextStyle(
		fontFamily = TypeScaleTokens.HeadlineLargeFont,
		fontWeight = TypeScaleTokens.HeadlineLargeWeight,
		fontSize = TypeScaleTokens.HeadlineLargeSize,
		lineHeight = TypeScaleTokens.HeadlineLargeLineHeight,
		letterSpacing = TypeScaleTokens.HeadlineLargeTracking,
	)
	val HeadlineMedium = TextStyle(
		fontFamily = TypeScaleTokens.HeadlineMediumFont,
		fontWeight = TypeScaleTokens.HeadlineMediumWeight,
		fontSize = TypeScaleTokens.HeadlineMediumSize,
		lineHeight = TypeScaleTokens.HeadlineMediumLineHeight,
		letterSpacing = TypeScaleTokens.HeadlineMediumTracking,
	)
	val HeadlineSmall = TextStyle(
		fontFamily = TypeScaleTokens.HeadlineSmallFont,
		fontWeight = TypeScaleTokens.HeadlineSmallWeight,
		fontSize = TypeScaleTokens.HeadlineSmallSize,
		lineHeight = TypeScaleTokens.HeadlineSmallLineHeight,
		letterSpacing = TypeScaleTokens.HeadlineSmallTracking,
	)
	val LabelLarge = TextStyle(
		fontFamily = TypeScaleTokens.LabelLargeFont,
		fontWeight = TypeScaleTokens.LabelLargeWeight,
		fontSize = TypeScaleTokens.LabelLargeSize,
		lineHeight = TypeScaleTokens.LabelLargeLineHeight,
		letterSpacing = TypeScaleTokens.LabelLargeTracking,
	)
	val LabelMedium = TextStyle(
		fontFamily = TypeScaleTokens.LabelMediumFont,
		fontWeight = TypeScaleTokens.LabelMediumWeight,
		fontSize = TypeScaleTokens.LabelMediumSize,
		lineHeight = TypeScaleTokens.LabelMediumLineHeight,
		letterSpacing = TypeScaleTokens.LabelMediumTracking,
	)
	val LabelSmall = TextStyle(
		fontFamily = TypeScaleTokens.LabelSmallFont,
		fontWeight = TypeScaleTokens.LabelSmallWeight,
		fontSize = TypeScaleTokens.LabelSmallSize,
		lineHeight = TypeScaleTokens.LabelSmallLineHeight,
		letterSpacing = TypeScaleTokens.LabelSmallTracking,
	)
	val TitleLarge = TextStyle(
		fontFamily = TypeScaleTokens.TitleLargeFont,
		fontWeight = TypeScaleTokens.TitleLargeWeight,
		fontSize = TypeScaleTokens.TitleLargeSize,
		lineHeight = TypeScaleTokens.TitleLargeLineHeight,
		letterSpacing = TypeScaleTokens.TitleLargeTracking,
	)
	val TitleMedium = TextStyle(
		fontFamily = TypeScaleTokens.TitleMediumFont,
		fontWeight = TypeScaleTokens.TitleMediumWeight,
		fontSize = TypeScaleTokens.TitleMediumSize,
		lineHeight = TypeScaleTokens.TitleMediumLineHeight,
		letterSpacing = TypeScaleTokens.TitleMediumTracking,
	)
	val TitleSmall = TextStyle(
		fontFamily = TypeScaleTokens.TitleSmallFont,
		fontWeight = TypeScaleTokens.TitleSmallWeight,
		fontSize = TypeScaleTokens.TitleSmallSize,
		lineHeight = TypeScaleTokens.TitleSmallLineHeight,
		letterSpacing = TypeScaleTokens.TitleSmallTracking,
	)
}

object TypeScaleTokens {
	val BodyLargeFont = TypefaceTokens.Plain
	val BodyLargeLineHeight = 24.0.sp
	val BodyLargeSize = 16.sp
	val BodyLargeTracking = 0.5.sp
	val BodyLargeWeight = TypefaceTokens.WeightRegular
	val BodyMediumFont = TypefaceTokens.Plain
	val BodyMediumLineHeight = 20.0.sp
	val BodyMediumSize = 14.sp
	val BodyMediumTracking = 0.2.sp
	val BodyMediumWeight = TypefaceTokens.WeightRegular
	val BodySmallFont = TypefaceTokens.Plain
	val BodySmallLineHeight = 16.0.sp
	val BodySmallSize = 12.sp
	val BodySmallTracking = 0.4.sp
	val BodySmallWeight = TypefaceTokens.WeightRegular
	val DisplayLargeFont = TypefaceTokens.Brand
	val DisplayLargeLineHeight = 64.0.sp
	val DisplayLargeSize = 57.sp
	val DisplayLargeTracking = -0.2.sp
	val DisplayLargeWeight = TypefaceTokens.WeightRegular
	val DisplayMediumFont = TypefaceTokens.Brand
	val DisplayMediumLineHeight = 52.0.sp
	val DisplayMediumSize = 45.sp
	val DisplayMediumTracking = 0.0.sp
	val DisplayMediumWeight = TypefaceTokens.WeightRegular
	val DisplaySmallFont = TypefaceTokens.Brand
	val DisplaySmallLineHeight = 44.0.sp
	val DisplaySmallSize = 36.sp
	val DisplaySmallTracking = 0.0.sp
	val DisplaySmallWeight = TypefaceTokens.WeightRegular
	val HeadlineLargeFont = TypefaceTokens.Brand
	val HeadlineLargeLineHeight = 40.0.sp
	val HeadlineLargeSize = 32.sp
	val HeadlineLargeTracking = 0.0.sp
	val HeadlineLargeWeight = TypefaceTokens.WeightRegular
	val HeadlineMediumFont = TypefaceTokens.Brand
	val HeadlineMediumLineHeight = 36.0.sp
	val HeadlineMediumSize = 28.sp
	val HeadlineMediumTracking = 0.0.sp
	val HeadlineMediumWeight = TypefaceTokens.WeightRegular
	val HeadlineSmallFont = TypefaceTokens.Brand
	val HeadlineSmallLineHeight = 32.0.sp
	val HeadlineSmallSize = 24.sp
	val HeadlineSmallTracking = 0.0.sp
	val HeadlineSmallWeight = TypefaceTokens.WeightRegular
	val LabelLargeFont = TypefaceTokens.Plain
	val LabelLargeLineHeight = 20.0.sp
	val LabelLargeSize = 14.sp
	val LabelLargeTracking = 0.1.sp
	val LabelLargeWeight = TypefaceTokens.WeightMedium
	val LabelMediumFont = TypefaceTokens.Plain
	val LabelMediumLineHeight = 16.0.sp
	val LabelMediumSize = 12.sp
	val LabelMediumTracking = 0.5.sp
	val LabelMediumWeight = TypefaceTokens.WeightMedium
	val LabelSmallFont = TypefaceTokens.Plain
	val LabelSmallLineHeight = 16.0.sp
	val LabelSmallSize = 11.sp
	val LabelSmallTracking = 0.5.sp
	val LabelSmallWeight = TypefaceTokens.WeightMedium
	val TitleLargeFont = TypefaceTokens.Brand
	val TitleLargeLineHeight = 28.0.sp
	val TitleLargeSize = 22.sp
	val TitleLargeTracking = 0.0.sp
	val TitleLargeWeight = TypefaceTokens.WeightRegular
	val TitleMediumFont = TypefaceTokens.Plain
	val TitleMediumLineHeight = 24.0.sp
	val TitleMediumSize = 16.sp
	val TitleMediumTracking = 0.2.sp
	val TitleMediumWeight = TypefaceTokens.WeightMedium
	val TitleSmallFont = TypefaceTokens.Plain
	val TitleSmallLineHeight = 20.0.sp
	val TitleSmallSize = 14.sp
	val TitleSmallTracking = 0.1.sp
	val TitleSmallWeight = TypefaceTokens.WeightMedium
}

object TypefaceTokens {
	val Brand = FontFamily.SansSerif
	val Plain = FontFamily.SansSerif
	val WeightBold = FontWeight.Bold
	val WeightMedium = FontWeight.Medium
	val WeightRegular = FontWeight.Normal
}

enum class TypographyKeyTokens {
	BodyLarge,
	BodyMedium,
	BodySmall,
	DisplayLarge,
	DisplayMedium,
	DisplaySmall,
	HeadlineLarge,
	HeadlineMedium,
	HeadlineSmall,
	LabelLarge,
	LabelMedium,
	LabelSmall,
	TitleLarge,
	TitleMedium,
	TitleSmall,
}
