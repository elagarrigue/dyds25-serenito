package edu.dyds.movies.data.local

import edu.dyds.movies.data.external.RemoteMovie

interface MoviesCache {
    fun getAll(): List<RemoteMovie>
    fun isEmpty(): Boolean
    fun saveAll(movies: List<RemoteMovie>)
    fun clear()
    fun getFromId(id: Int): RemoteMovie?
}