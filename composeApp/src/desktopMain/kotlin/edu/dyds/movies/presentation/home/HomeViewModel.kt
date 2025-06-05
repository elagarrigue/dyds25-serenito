package edu.dyds.movies.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.detail.DetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    fun getAllMovies() {
        viewModelScope.launch {
            _state.emit(HomeState(isLoading = true))
            val movies = getMoviesUseCase.invokePopularMovies()
            _state.emit(HomeState(isLoading = false, movies = movies))
        }
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val movies: List<QualifiedMovie> = emptyList()
)