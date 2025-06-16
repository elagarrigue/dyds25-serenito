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
            _state.emit(DetailState(isLoading = true))
            try {
                val movie = getMovieDetailUseCase.invoke(id)
                _state.emit(DetailState(isLoading = false, movie = movie))
            } catch (e: Exception) {
                e.message
                _state.emit(DetailState(isLoading = false, movie = null)) //Consultar esto a Ema
            }
        }
    }
}

data class DetailState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)