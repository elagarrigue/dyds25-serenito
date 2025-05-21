package edu.dyds.movies.domain.usecase
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.entity.Movie

//Intermediarios entre los ViewModel y Repository, este en particular se encarga de obtener peliculas del repositorio

class GetPopularMoviesUseCase(
    private val repository: MoviesRepository
){
    fun invokePopularMovies() : List<Movie>{
        return repository.getPopularMovies()
    }
}