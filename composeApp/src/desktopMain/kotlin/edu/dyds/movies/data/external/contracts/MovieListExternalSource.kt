package edu.dyds.movies.data.external.contracts

import edu.dyds.movies.domain.entity.Movie

interface MoviesListExternalSource {
    suspend fun getPopularMovies(): List<Movie>
}
