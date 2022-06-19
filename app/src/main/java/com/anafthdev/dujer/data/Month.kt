package com.anafthdev.dujer.data

enum class Month {
	JANUARY,
	FEBRUARY,
	MARCH,
	APRIL,
	MAY,
	JUNE,
	JULY,
	AUGUST,
	SEPTEMBER,
	OCTOBER,
	NOVEMBER,
	DECEMBER
}

fun Month.fromString(s: String): Month? {
	return when (s.uppercase()) {
		Month.JANUARY.name.uppercase() -> Month.JANUARY
		Month.FEBRUARY.name.uppercase() -> Month.FEBRUARY
		Month.MARCH.name.uppercase() -> Month.MARCH
		Month.APRIL.name.uppercase() -> Month.APRIL
		Month.MAY.name.uppercase() -> Month.MAY
		Month.JUNE.name.uppercase() -> Month.JUNE
		Month.JULY.name.uppercase() -> Month.JULY
		Month.AUGUST.name.uppercase() -> Month.AUGUST
		Month.SEPTEMBER.name.uppercase() -> Month.SEPTEMBER
		Month.OCTOBER.name.uppercase() -> Month.OCTOBER
		Month.NOVEMBER.name.uppercase() -> Month.NOVEMBER
		Month.DECEMBER.name.uppercase() -> Month.DECEMBER
		else -> null
	}
}
