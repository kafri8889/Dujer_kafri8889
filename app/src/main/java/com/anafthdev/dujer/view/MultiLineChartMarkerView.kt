package com.anafthdev.dujer.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.anafthdev.dujer.R
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import timber.log.Timber

@SuppressLint("ViewConstructor")
class MultiLineChartMarkerView(
	context: Context,
	private val incomeLineDataSetEntry: List<Entry>,
	private val expenseLineDataSetEntry: List<Entry>
): MarkerView(context, R.layout.multi_line_chart_marker) {

	private val tvIncomeAmount = findViewById<TextView>(R.id.tvIncomeAmount_MultiLineChartMarker)
	private val tvExpenseAmount = findViewById<TextView>(R.id.tvExpenseAmount_MultiLineChartMarker)
	
	@SuppressLint("SetTextI18n")
	override fun refreshContent(e: Entry?, highlight: Highlight?) {
		Timber.i("e: ${e.toString()}")
		
		if (e != null) {
			val incomeEntry = incomeLineDataSetEntry[e.x.toInt()]
			val expenseEntry = expenseLineDataSetEntry[e.x.toInt()]
			
			tvIncomeAmount.text = CurrencyFormatter.format(
				locale = AppUtil.deviceLocale,
				amount = incomeEntry.y.toDouble(),
			)
			
			tvExpenseAmount.text = CurrencyFormatter.format(
				locale = AppUtil.deviceLocale,
				amount = expenseEntry.y.toDouble(),
			)
		} else {
			tvIncomeAmount.text = CurrencyFormatter.format(
				locale = AppUtil.deviceLocale,
				amount = 0.0,
			)
			
			tvExpenseAmount.text = CurrencyFormatter.format(
				locale = AppUtil.deviceLocale,
				amount = 0.0,
			)
		}
		
		super.refreshContent(e, highlight)
	}
	
	override fun getOffset(): MPPointF {
		// -(width / 2) center IMarker horizontal
		// (-height / 2) center IMarker vertical
		return MPPointF((-width).toFloat(), (-height).toFloat())
	}
}