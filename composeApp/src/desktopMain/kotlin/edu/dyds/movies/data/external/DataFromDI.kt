package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.entity.RemoteResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class DataFromDI(private val tmdbHttpClient: HttpClient,
) {
    private val cacheMovies: MutableList<RemoteMovie> = mutableListOf()

    suspend fun getTMDBMovieDetails(id: Int): String {
        return tmdbHttpClient.get("/3/movie/$id").body()
    }

    suspend fun getTMDBPopularMovies(): String {
        return tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()
    }
}