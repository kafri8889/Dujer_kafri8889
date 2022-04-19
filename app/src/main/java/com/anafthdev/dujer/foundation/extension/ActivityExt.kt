package com.anafthdev.dujer.foundation.extension

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker

fun AppCompatActivity.showDatePicker(
	selection: Long = MaterialDatePicker.todayInUtcMilliseconds(),
	onCancel: () -> Unit = {},
	onPick: (Long) -> Unit
) {
	
	val datePicker = MaterialDatePicker.Builder.datePicker()
			.setTitleText("Select date")
			.setSelection(selection)
			.build()
	
	datePicker.addOnPositiveButtonClickListener(onPick)
	datePicker.addOnCancelListener { onCancel() }
	datePicker.addOnNegativeButtonClickListener { onCancel() }
	datePicker.show(this.supportFragmentManager, "ActivityExt")
}
