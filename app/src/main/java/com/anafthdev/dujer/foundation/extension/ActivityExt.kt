package com.anafthdev.dujer.foundation.extension

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker

fun AppCompatActivity.showDatePicker(
	selection: Long = MaterialDatePicker.todayInUtcMilliseconds(),
	onPick: (Long) -> Unit
) {
	
	val datePicker = MaterialDatePicker.Builder.datePicker()
			.setTitleText("Select date")
			.setSelection(selection)
			.build()
	
	datePicker.addOnPositiveButtonClickListener(onPick)
	datePicker.show(this.supportFragmentManager, "ActivityExt")
}
