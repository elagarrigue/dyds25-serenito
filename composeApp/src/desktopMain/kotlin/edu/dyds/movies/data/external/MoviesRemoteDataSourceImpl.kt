package edu.dyds.movies.data.external

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class MoviesRemoteDataSourceImpl(
    private val client: HttpClient
) : MoviesRemoteDataSource {
    override suspend fun getPopularMovies(): RemoteResult {
        return client.get("/3/discover/movie?sort_by=popularity.desc").body()
    }

    override suspend fun getMovieDetails(id: Int): RemoteMovie {
        return client.get("/3/movie/$id").body()
    }
}