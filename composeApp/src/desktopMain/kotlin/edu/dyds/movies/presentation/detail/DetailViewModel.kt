package edu.dyds.movies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.GetMovieDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            val movie = getMovieDetailUseCase.invoke(id)
            _state.emit(DetailState(isLoading = false, movie = movie))
        }
    }
}

data class DetailState(
    val isLoading: Boolean = true,
    val movie: Movie? = null
)