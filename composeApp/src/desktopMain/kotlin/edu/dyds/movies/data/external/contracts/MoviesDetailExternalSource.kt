package edu.dyds.movies.data.external.contracts

import edu.dyds.movies.domain.entity.Movie

interface MoviesDetailExternalSource {
    suspend fun getMovieByTitle(title: String): Movie?
}