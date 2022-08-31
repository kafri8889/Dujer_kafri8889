package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.feature.edit_wallet_balance.data.EditBalanceOption

fun EditBalanceOption.isChangeBalance(): Boolean = this == EditBalanceOption.CHANGE_BALANCE
fun EditBalanceOption.isChangeInitialAmount(): Boolean = this == EditBalanceOption.CHANGE_INITIAL_AMOUNT
