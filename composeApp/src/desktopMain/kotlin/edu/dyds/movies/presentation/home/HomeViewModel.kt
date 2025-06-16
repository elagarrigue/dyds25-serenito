package edu.dyds.movies.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
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
            try {
                val movies = getMoviesUseCase.invoke()
                _state.emit(HomeState(isLoading = false, movies = movies))
            } catch (e: Exception) {
                _state.emit(HomeState(isLoading = false, movies = emptyList()))
            }
        }
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val movies: List<QualifiedMovie> = emptyList()
)