package edu.dyds.movies.data.external.omdb

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.data.external.contracts.MoviesDetailExternalSource

class OMDBMoviesDetailSource(
    private val client: HttpClient
) : MoviesDetailExternalSource {

    override suspend fun getMovieByTitle(title: String): Movie? {
        val response = client.get {
            parameter("t", title)
        }
        return response.body<RemoteOMDBMovie>().toDomainMovie()
    }
}