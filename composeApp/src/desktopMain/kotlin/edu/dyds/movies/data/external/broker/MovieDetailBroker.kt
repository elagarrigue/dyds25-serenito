package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.contracts.MoviesDetailExternalSource
import edu.dyds.movies.domain.entity.Movie

class MoviesDetailBroker(
    private val tmdbSource: MoviesDetailExternalSource,
    private val omdbSource: MoviesDetailExternalSource
) : MoviesDetailExternalSource {

    override suspend fun getMovieByTitle(title: String): Movie? {
        val tmdb = tmdbSource.getMovieByTitle(title)
        val omdb = omdbSource.getMovieByTitle(title)

        val movie = when {
            tmdb != null && omdb != null -> buildMovie(tmdb, omdb)
            tmdb != null -> tmdb.copy(overview = "TMDB: ${tmdb.overview}")
            omdb != null -> omdb.copy(overview = "OMDB: ${omdb.overview}")
            else -> null
        }

        println("Resultado combinado para '$title':\n${movie?.overview ?: "Ningún resultado"}")
        return movie
    }

    private fun buildMovie(
        tmdbMovie: Movie,
        omdbMovie: Movie
    ) = Movie(
        id = tmdbMovie.id,
        title = tmdbMovie.title,
        overview = "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
        releaseDate = tmdbMovie.releaseDate,
        poster = tmdbMovie.poster,
        backdrop = tmdbMovie.backdrop,
        originalTitle = tmdbMovie.originalTitle,
        originalLanguage = tmdbMovie.originalLanguage,
        popularity = (tmdbMovie.popularity + omdbMovie.popularity) / 2.0,
        voteAverage = (tmdbMovie.voteAverage + omdbMovie.voteAverage) / 2.0
    )
}
