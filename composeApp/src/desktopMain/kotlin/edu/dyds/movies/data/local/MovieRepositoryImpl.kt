package edu.dyds.movies.data.local

import edu.dyds.movies.data.external.MoviesRemoteDataSource
import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.repository.MoviesRepository

private const val MIN_VOTE_AVERAGE = 6.0

class MoviesRepositoryImpl(
    private val remoteDataSource: MoviesRemoteDataSource
) : MoviesRepository {
    private val cacheMovies: MutableList<RemoteMovie> = mutableListOf()

    override suspend fun getPopularMovies(): List<QualifiedMovie> {
        val movies = cacheMovies.ifEmpty {
            try {
                remoteDataSource.getPopularMovies().results.apply {
                    cacheMovies.clear()
                    cacheMovies.addAll(this)
                }
            } catch (e: Exception) {
                e.message
                emptyList()
            }
        }

        return movies.sortAndMap()
    }

    override suspend fun getMovieDetails(id: Int): Movie? {
        return try {
            remoteDataSource.getMovieDetails(id).toDomainMovie()
        } catch (e: Exception) {
            e.message
            null
        }
    }

    private fun List<RemoteMovie>.sortAndMap(): List<QualifiedMovie> {
        return this
            .sortedByDescending { it.voteAverage }
            .map {
                QualifiedMovie(
                    movie = it.toDomainMovie(),
                    isGoodMovie = it.voteAverage >= MIN_VOTE_AVERAGE
                )
            }
    }
}