package edu.dyds.movies.data.external
import edu.dyds.movies.domain.entity.Movie

interface MoviesExternalDataSource {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getMovieDetails(title: String): Movie?
}

