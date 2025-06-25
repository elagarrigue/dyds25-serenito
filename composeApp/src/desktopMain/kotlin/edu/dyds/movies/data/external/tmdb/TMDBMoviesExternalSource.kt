package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.contracts.MoviesListExternalSource
import edu.dyds.movies.data.external.contracts.MoviesDetailExternalSource
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TMDBMoviesExternalSource(
    private val client: HttpClient
) : MoviesListExternalSource, MoviesDetailExternalSource {

    override suspend fun getPopularMovies(): List<Movie> {
        val response = client.get("/3/discover/movie?sort_by=popularity.desc")
        val remoteResult = response.body<RemoteResult>()
        return remoteResult.results.map { it.toDomainMovie() }
    }

    override suspend fun getMovieByTitle(title: String): Movie? {
        val result = client.get("/3/search/movie?query=$title").body<RemoteResult>()
        return result.results.firstOrNull()?.toDomainMovie()
    }
}
