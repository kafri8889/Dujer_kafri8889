package com.anafthdev.dujer.foundation.common

import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker

fun FragmentActivity.showDatePicker(
	min: Long = 0,
	selection: Long = MaterialDatePicker.todayInUtcMilliseconds(),
	onCancel: () -> Unit = {},
	onPick: (Long) -> Unit
) {
	val datePicker = MaterialDatePicker.Builder.datePicker()
		.setTitleText("Pick Date")
		.setSelection(selection)
		.setCalendarConstraints(CalendarConstraints.Builder().setStart(min).build())
		.build()
	
	datePicker.addOnPositiveButtonClickListener { onPick(it) }
	datePicker.addOnCancelListener { onCancel() }
	datePicker.show(supportFragmentManager, datePicker.tag)
}
