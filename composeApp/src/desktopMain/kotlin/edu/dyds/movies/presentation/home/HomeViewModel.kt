package edu.dyds.movies.presentation.home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//Como tenemos MoviesViewModel, que se encarga de manejar la lógica de la pantalla principal (lista de peliculas) y de la pantalla para los detalles
//Lo dividimos en dos para cumplir con el principio S de SOLID, donde tenemos entonces por separado el manejo de esas dos pantallas

private const val MIN_VOTE_AVERAGE = 6.0

class HomeViewModel (
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel(){
    private val moviesStateMutableStateFlow = MutableStateFlow(MoviesUiState())
    val state: StateFlow<MoviesUiState> = moviesStateMutableStateFlow.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies(){
        viewModelScope.launch {
            try{
                moviesStateMutableStateFlow.update { it.copy(isLoading = true) }

                val movies = getPopularMoviesUseCase.invokePopularMovies()
                    .sortedByDescending { it.voteAverage }
                    .map { movie : Movie ->
                        QualifiedMovie(
                            movie = movie,
                            isGoodMovie = movie.voteAverage >= MIN_VOTE_AVERAGE
                        )
                    }

                moviesStateMutableStateFlow.update {
                    it.copy(
                        isLoading = true,
                        movies = movies
                    )
                }

            } catch (e: Exception){
                moviesStateMutableStateFlow.update { it.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar las peliculas"
                ) }
            }


        }
    }

    data class MoviesUiState(
        val isLoading: Boolean = false,
        val movies: List<QualifiedMovie> = emptyList(),
        val errorMessage : String? = null
    )
}