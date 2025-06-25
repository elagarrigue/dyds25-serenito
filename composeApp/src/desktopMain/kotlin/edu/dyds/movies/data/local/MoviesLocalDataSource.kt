package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie


interface MoviesLocalDataSource {
    fun getAll(): List<Movie>
    fun isEmpty(): Boolean
    fun saveAll(movies: List<Movie>)
    fun clear()
}