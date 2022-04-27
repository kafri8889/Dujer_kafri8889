package com.anafthdev.dujer.ui.search

sealed class SearchAction {
	data class Search(val query: String): SearchAction()
}
