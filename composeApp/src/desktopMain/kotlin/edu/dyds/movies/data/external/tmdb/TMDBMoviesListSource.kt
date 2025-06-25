package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.contracts.MoviesListExternalSource
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TMDBMoviesListSource(private val client: HttpClient) : MoviesListExternalSource {
    override suspend fun getPopularMovies(): List<Movie> {
        val response = client.get("/3/discover/movie?sort_by=popularity.desc")
        val remoteResult = response.body<RemoteResult>()
        return remoteResult.results.map { it.toDomainMovie() }
    }
}
