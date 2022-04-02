package com.anafthdev.dujer.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.model.Currency

@Entity(tableName = "financial")
data class Financial(
	@PrimaryKey val id: Int,
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "amount") var amount: Double,
	@ColumnInfo(name = "type") var type: FinancialType,
	@ColumnInfo(name = "category") var category: Category,
	@ColumnInfo(name = "currency") var currency: Currency,
	@ColumnInfo(name = "dateCreated") var dateCreated: Long,
)
