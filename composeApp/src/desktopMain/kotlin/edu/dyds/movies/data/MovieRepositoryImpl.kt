package edu.dyds.movies.data

import edu.dyds.movies.data.local.MoviesLocalDataSource
import edu.dyds.movies.data.external.MoviesExternalDataSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(
    private val remoteDataSource: MoviesExternalDataSource,
    private val localDataSource: MoviesLocalDataSource
) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return try {
            if (localDataSource.isEmpty()) {
                val remoteMovies = remoteDataSource.getPopularMovies()
                localDataSource.saveAll(remoteMovies)
                remoteMovies
            } else {
                localDataSource.getAll()
            }
        } catch (e: Exception) {
            e.message?.let { println(it) }
            emptyList()
        }
    }

    override suspend fun getMovieDetails(title: String): Movie? {
        println("BUSCANDO DETALLES PARA: $title")
        return try {
            remoteDataSource.getMovieDetails(title)
        } catch (e: Exception) {
            e.message?.let { println(it) }
            null
        }
    }

}
