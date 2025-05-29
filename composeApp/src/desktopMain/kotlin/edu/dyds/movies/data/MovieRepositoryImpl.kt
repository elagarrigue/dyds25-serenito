package edu.dyds.movies.data

import edu.dyds.movies.data.local.MoviesCache
import edu.dyds.movies.data.external.MoviesRemoteDataSource
import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.repository.MoviesRepository

private const val MIN_VOTE_AVERAGE = 6.0

class MoviesRepositoryImpl(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val cache: MoviesCache
) : MoviesRepository {

    override suspend fun getPopularMovies(): List<QualifiedMovie> {
        val movies = if (cache.isEmpty()) {
            try {
                remoteDataSource.getPopularMovies().results.also {
                    cache.saveAll(it)
                }
            } catch (e: Exception) {
                e.message
                emptyList()
            }
        } else {
            cache.getAll()
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
