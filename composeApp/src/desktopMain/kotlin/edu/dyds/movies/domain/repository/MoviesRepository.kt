package edu.dyds.movies.domain.repository
import edu.dyds.movies.domain.entity.Movie

interface MoviesRepository {
    fun getPopularMovies() : List<Movie>
    fun getMovieDetails(id: Int): Movie?
}