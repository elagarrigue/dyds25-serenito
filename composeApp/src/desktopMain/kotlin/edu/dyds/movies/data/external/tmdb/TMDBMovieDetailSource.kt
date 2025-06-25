package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.contracts.MoviesDetailExternalSource
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TMDBMoviesDetailSource(private val client: HttpClient) : MoviesDetailExternalSource {
    override suspend fun getMovieByTitle(title: String): Movie? {
        val result = client.get("/3/search/movie?query=$title").body<RemoteResult>()
        return result.results.firstOrNull()?.toDomainMovie()
    }
}
