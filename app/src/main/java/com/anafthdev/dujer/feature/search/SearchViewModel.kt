package com.anafthdev.dujer.feature.search

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.feature.search.environment.ISearchEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	searchEnvironment: ISearchEnvironment
): StatefulViewModel<SearchState, Unit, SearchAction, ISearchEnvironment>(SearchState(), searchEnvironment) {
	
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
	
	override fun dispatch(action: SearchAction) {
		when (action) {
			is SearchAction.Search -> {
				viewModelScope.launch {
					environment.search(action.query)
					setState {
						copy(
							textQuery = action.query
						)
					}
				}
			}
		}
	}
	
}