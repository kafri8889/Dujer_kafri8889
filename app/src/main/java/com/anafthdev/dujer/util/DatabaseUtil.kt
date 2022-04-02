package com.anafthdev.dujer.util

import com.anafthdev.dujer.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class DatabaseUtil(
	private val appDatabase: AppDatabase
) {
	
	private val scope = CoroutineScope(Job() + Dispatchers.IO)
}