package com.anafthdev.dujer.foundation.common.csv

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.foundation.common.AppUtil.dateFormatter
import com.anafthdev.dujer.foundation.common.AppUtil.toast
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.uppercaseFirstWord
import com.anafthdev.dujer.model.Currency
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File

object CSVWriter  {
	
	val SAVE_DIR: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
	
	fun writeFinancial(
		context: Context,
		fileName: String,
		currency: Currency,
		wallets: List<Wallet>,
		financials: List<Financial>
	): Boolean {
		val saveDir = SAVE_DIR
		if (!saveDir.exists()) saveDir.mkdirs()
		
		val file = File("${saveDir.absolutePath}/$fileName")
		
		try {
			if (!file.exists()) {
				file.createNewFile()
			} else {
				"File exists, export aborted".toast(context)
				return false
			}
			
			csvWriter {
				delimiter = ','
			}.open(file) {
				
				// Add header
				writeRow(
					listOf(
						"Name",
						"Amount",
						"Type",
						"Category",
						"Wallet",
						"Date"
					)
				)
				
				financials.forEach { financial ->
					val name = financial.name
					val amount = CurrencyFormatter.format(
						locale = deviceLocale,
						amount = financial.amount,
						useSymbol = true,
						currencyCode = currency.countryCode
					)
					val type = financial.type.name.uppercaseFirstWord(true)
					val category = financial.category.name
					val wallet = wallets.find { it.id == financial.walletID }?.name ?: "-"
					val date = dateFormatter.format(financial.dateCreated)
					
					writeRow(
						name,
						amount,
						type,
						category,
						wallet,
						date
					)
				}
			}
			
			context.getString(
				R.string.saved_to_path,
				"${saveDir.path}/$fileName"
			).toast(context, Toast.LENGTH_LONG)
			
			return true
		} catch (e: Exception) {
			"Export failed".toast(context)
			e.message.toast(context)
			return false
		}
	}
	
//	fun readFinancial(
//		context: Context,
//		fileName: String
//	) {
//		val (
//			nameColumn,
//			amountColumn,
//			typeColumn,
//			categoryColumn,
//			walletColumn,
//			dateColumn
//		) = Hexad(0, 1, 2, 3, 4, 5)
//
//		val path = Path("${context.cacheDir.absolutePath}/$fileName")
//		val reader = CsvReader.builder().build(path)
//
//		val financials = arrayListOf<Financial>()
//
//		reader.forEach {
//			financials.add(
//				Financial(
//
//				)
//			)
//		}
//	}
}
