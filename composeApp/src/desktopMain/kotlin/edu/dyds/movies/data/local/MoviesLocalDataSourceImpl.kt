package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

class MoviesLocalDataSourceImpl : MoviesLocalDataSource {
    private val cachedMovies: MutableList<Movie> = mutableListOf()

    override fun getAll(): List<Movie> = cachedMovies

    override fun isEmpty(): Boolean = cachedMovies.isEmpty()

    override fun saveAll(movies: List<Movie>) {
        cachedMovies.clear()
        cachedMovies.addAll(movies)
    }

    override fun clear() {
        cachedMovies.clear()
    }

    override fun getFromTitle(title: String): Movie? {
        println("Buscando en localDataSource con título: $title")
        return null
    }
}