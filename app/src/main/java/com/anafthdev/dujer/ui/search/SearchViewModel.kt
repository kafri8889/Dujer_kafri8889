package com.anafthdev.dujer.ui.search

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.search.environment.ISearchEnvironment
import com.anafthdev.dujer.ui.search.environment.SearchEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	searchEnvironment: ISearchEnvironment
): StatefulViewModel<SearchState, Unit, ISearchEnvironment>(SearchState(), searchEnvironment) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getResultFinancial().collect { result ->
				setState {
					copy(
						resultFinancial = result
					)
				}
			}
			
			environment.getResultCategory().collect { result ->
				setState {
					copy(
						resultCategory = result
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getResultCategory().collect { result ->
				setState {
					copy(
						resultCategory = result
					)
				}
			}
		}
	}
	
	fun search(s: String) {
		viewModelScope.launch(environment.dispatcher) {
			environment.search(s)
			setState {
				copy(
					textQuery = s
				)
			}
		}
	}
	
}