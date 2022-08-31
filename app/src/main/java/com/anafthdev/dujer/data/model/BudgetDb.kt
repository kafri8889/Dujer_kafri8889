package com.anafthdev.dujer.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "budget_table")
@Parcelize
data class BudgetDb(
	@PrimaryKey @ColumnInfo(name = "budget_id") val id: Int,
	@ColumnInfo(name = "budget_max") val max: Double,
	@ColumnInfo(name = "budget_categoryID") val categoryID: Int,
	@ColumnInfo(name = "budget_remaining") val remaining: Double = 0.0,
	@ColumnInfo(name = "budget_isMaxReached") val isMaxReached: Boolean = false,
): Parcelable
