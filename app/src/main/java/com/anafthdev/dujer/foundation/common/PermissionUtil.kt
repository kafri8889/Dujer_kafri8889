package com.anafthdev.dujer.foundation.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionUtil {
	
	fun checkPermission(
		context: Context,
		permissions: Array<String>
	): Boolean {
		return permissions.all {
			ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
		}
	}
	
}