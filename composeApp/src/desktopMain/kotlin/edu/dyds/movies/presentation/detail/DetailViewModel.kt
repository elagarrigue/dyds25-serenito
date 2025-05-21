package edu.dyds.movies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.entity.RemoteResult
import edu.dyds.movies.presentation.MoviesViewModel.MovieDetailUiState
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

//Como tenemos MoviesViewModel, que se encarga de manejar la lógica de la pantalla principal (lista de peliculas) y de la pantalla para los detalles
//Lo dividimos en dos para cumplir con el principio S de SOLID, donde tenemos entonces por separado el manejo de esas dos pantallas

class DetailViewModel(
    private val tmdbHttpClient: HttpClient,
) : ViewModel() {
    private val movieDetailStateMutableStateFlow = MutableStateFlow(MovieDetailUiState())
    val movieDetailStateFlow: Flow<MovieDetailUiState> = movieDetailStateMutableStateFlow

    fun getMovieDetail(id: Int){
        viewModelScope.launch {
            movieDetailStateMutableStateFlow.emit(
                MovieDetailUiState(isLoading = true)
            )
            movieDetailStateMutableStateFlow.emit(
                MovieDetailUiState (
                    isLoading = false,
                    movie = getMovieDetails(id)?.toDomainMovie()
                )
            )
        }
    }

    private suspend fun getMovieDetails(id: Int): RemoteMovie? =
        try{
            getTMDBMOvieDetais(id)
        } catch (e: Exception){
            null
        }


    private suspend fun getTMDBMOvieDetais(id: Int): RemoteMovie =
        tmdbHttpClient.get("/3/movie/$id").body()

    data class MovieDetailUiState(
        val isLoading: Boolean = false,
        val movie: Movie? = null,
    )
}