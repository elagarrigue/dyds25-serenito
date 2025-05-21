package edu.dyds.movies.data.local

import edu.dyds.movies.data.external.DataFromDI
import edu.dyds.movies.di.MoviesDependencyInjector.getDataFromDI

class MovieRepositoryImpl {
    private val externalData = getDataFromDI()

    private suspend fun getMovieDetails(id: Int) =
        try {
            externalData.getTMDBMovieDetails(id)
        } catch (e: Exception) {
            null
        }

    private suspend fun getPopularMovies() =
        try {
            externalData.getTMDBPopularMovies()
        } catch (e: Exception) {
            null
        }
}