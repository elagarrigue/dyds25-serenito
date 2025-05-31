package edu.dyds.movies.data.local

import edu.dyds.movies.data.external.RemoteMovie

class MoviesCacheImpl : MoviesCache {
    private val cachedMovies: MutableList<RemoteMovie> = mutableListOf()

    override fun getAll(): List<RemoteMovie> = cachedMovies

    override fun isEmpty(): Boolean = cachedMovies.isEmpty()

    override fun saveAll(movies: List<RemoteMovie>) {
        cachedMovies.clear()
        cachedMovies.addAll(movies)
    }

    override fun clear() {
        cachedMovies.clear()
    }

    override fun getFromId(id: Int): RemoteMovie? {
        return cachedMovies.find { movie -> movie.id == id }
    }
}