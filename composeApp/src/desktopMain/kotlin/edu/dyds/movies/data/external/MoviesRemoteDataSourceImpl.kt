package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class MoviesRemoteDataSourceImpl(
    private val client: HttpClient
) : MoviesRemoteDataSource {
    override suspend fun getPopularMovies(): List<Movie> {
        val remoteResult = client.get("/3/discover/movie?sort_by=popularity.desc").body<RemoteResult>()
        return remoteResult.results.map { it.toDomainMovie() }
    }

    override suspend fun getMovieDetails(id: Int): Movie {
        val remoteMovie = client.get("/3/movie/$id").body<RemoteMovie>()
        return remoteMovie.toDomainMovie()
    }
}