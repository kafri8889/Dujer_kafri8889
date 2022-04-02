package com.anafthdev.dujer.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.anafthdev.dujer.R
import com.anafthdev.dujer.model.Currency
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

@SuppressLint("ViewConstructor")
class LineChartMarkerView(
	context: Context,
	private val currency: Currency
): MarkerView(context, R.layout.line_chart_marker), IMarker {
	
	private val tvAmount = findViewById<TextView>(R.id.tvAmount_LineChartMarker)
	
	@SuppressLint("SetTextI18n")
	override fun refreshContent(e: Entry, highlight: Highlight?) {
		tvAmount.text = "${currency.symbol} ${e.y}"
		
		super.refreshContent(e, highlight)
	}
	
	override fun getOffset(): MPPointF {
		// -(width / 2) center IMarker horizontal
		// (-height / 2) center IMarker vertical
		return MPPointF((-width).toFloat(), (-height).toFloat())
	}
}