package com.anafthdev.dujer.util

import android.graphics.Color
import java.util.*

object ColorTemplate {
	
	const val COLOR_NONE = 0x00112233
	
	val LIBERTY_COLORS = intArrayOf(
		Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
		Color.rgb(118, 174, 175), Color.rgb(42, 109, 130)
	)
	val JOYFUL_COLORS = intArrayOf(
		Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
		Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)
	)
	val PASTEL_COLORS = intArrayOf(
		Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
		Color.rgb(191, 134, 134), Color.rgb(179, 48, 80)
	)
	val COLORFUL_COLORS = intArrayOf(
		Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
		Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
	)
	val VORDIPLOM_COLORS = intArrayOf(
		Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
		Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
	)
	val MATERIAL_COLORS = intArrayOf(
		rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")
	)
	
	/**
	 * Converts the given hex-color-string to rgb.
	 *
	 * @param hex
	 * @return
	 */
	fun rgb(hex: String): Int {
		val color = hex.replace("#", "").toLong(16).toInt()
		val r = color shr 16 and 0xFF
		val g = color shr 8 and 0xFF
		val b = color shr 0 and 0xFF
		return Color.rgb(r, g, b)
	}
	
	fun getRandomColor(): Int {
		val rnd = Random()
		return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
	}
	
	fun getRandomColor(size: Int): List<Int> {
		val colors = arrayListOf<Int>()
		
		repeat(size) {
			colors.add(getRandomColor())
		}
		
		return colors
	}
	
	fun getRandomColorApproach(): Int {
		return (Math.random() * 16777215).toInt() or (0xFF shl 24)
	}
	
	internal class RandomColorSemiApproach {
		private val recycle: Stack<Int> = Stack()
		private val colors: Stack<Int> = Stack()
		
		val color: Int
			get() {
				if (colors.isEmpty()) {
					while (!recycle.isEmpty()) colors.push(recycle.pop())
					Collections.shuffle(colors)
				}
				val c: Int = colors.pop()
				recycle.push(c)
				return c
			}
		
		init {
			recycle.addAll(
				listOf(
					-0xbbcca, -0x16e19d, -0x63d850, -0x98c549,
					-0xc0ae4b, -0xde690d, -0xfc560c, -0xff432c,
					-0xff6978, -0xb350b0, -0x743cb6, -0x3223c7,
					-0x14c5, -0x3ef9, -0x6800, -0xa8de,
					-0x86aab8, -0x616162, -0x9f8275, -0xcccccd
				)
			)
		}
	}
	
}