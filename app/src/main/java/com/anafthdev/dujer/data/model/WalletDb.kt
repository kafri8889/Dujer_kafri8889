package com.anafthdev.dujer.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.dujer.model.CategoryTint
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity(tableName = "wallet_table")
@Parcelize
data class WalletDb(
	@PrimaryKey @ColumnInfo(name = "wallet_id") val id: Int,
	@ColumnInfo(name = "wallet_name") var name: String,
	@ColumnInfo(name = "wallet_initialBalance") var initialBalance: Double,
	@ColumnInfo(name = "wallet_iconID") var iconID: Int,
	@ColumnInfo(name = "wallet_tint") val tint: @RawValue CategoryTint,
	@ColumnInfo(name = "wallet_defaultWallet") val defaultWallet: Boolean,
): Parcelable
