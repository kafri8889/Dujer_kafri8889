package com.anafthdev.dujer.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.model.CategoryTint
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity(tableName = "category_table")
@Parcelize
data class CategoryDb(
	@PrimaryKey @ColumnInfo(name = "category_id") val id: Int,
	@ColumnInfo(name = "category_name") var name: String,
	@ColumnInfo(name = "category_iconID") var iconID: Int,
	@ColumnInfo(name = "category_tint") var tint: @RawValue CategoryTint,
	@ColumnInfo(name = "category_type") var type: @RawValue FinancialType,
	@ColumnInfo(name = "category_defaultCategory") var defaultCategory: Boolean = false
): Parcelable
