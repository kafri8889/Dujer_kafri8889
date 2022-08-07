package com.anafthdev.dujer.feature.search

sealed class SearchAction {
	data class Search(val query: String): SearchAction()
}
