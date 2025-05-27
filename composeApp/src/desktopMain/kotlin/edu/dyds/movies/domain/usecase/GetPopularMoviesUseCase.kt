package edu.dyds.movies.domain.usecase
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie

//Intermediarios entre los ViewModel y Repository, este en particular se encarga de obtener peliculas del repositorio

class GetPopularMoviesUseCase constructor(
    private val repository: MoviesRepository
) {
    suspend fun invokePopularMovies(): List<QualifiedMovie> {
        return repository.getPopularMovies()
    }
}