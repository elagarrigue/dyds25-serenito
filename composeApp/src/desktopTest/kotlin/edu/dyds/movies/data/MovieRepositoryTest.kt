package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesRemoteDataSource
import edu.dyds.movies.data.external.contracts.MoviesDetailExternalSource
import edu.dyds.movies.data.local.MoviesLocalDataSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.fakes.TestDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryTest {

    private lateinit var sampleMovie: Movie
    private lateinit var local: FakeLocalDataSource
    private lateinit var remote: FakeRemoteDataSource
    private lateinit var repository: MoviesRepositoryImpl

    @Before
    fun setup() {
        sampleMovie = TestDataFactory.createMovie(1)
        local = FakeLocalDataSource(movies = mutableListOf(sampleMovie))
        remote = FakeRemoteDataSource()
        repository = MoviesRepositoryImpl(remote, local)
    }

    @Test
    fun `getPopularMovies returns local data if not empty`() = runTest {
        val result = repository.getPopularMovies()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(sampleMovie.title, result.first().title)
    }

    @Test
    fun `getPopularMovies fetches from remote if local is empty`() = runTest {
        local = FakeLocalDataSource(movies = mutableListOf())
        remote = FakeRemoteDataSource(movies = listOf(sampleMovie))
        repository = MoviesRepositoryImpl(remote, local)

        val result = repository.getPopularMovies()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(sampleMovie.title, result.first().title)
        Assert.assertEquals(1, local.getAll().size)
    }

    @Test
    fun `getPopularMovies returns empty list on error`() = runTest {
        local = FakeLocalDataSource(shouldThrow = true)
        remote = FakeRemoteDataSource()
        repository = MoviesRepositoryImpl(remote, local)

        val result = repository.getPopularMovies()

        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun `getMovieDetails returns null on error`() = runTest {
        local = FakeLocalDataSource(shouldThrow = true)
        remote = FakeRemoteDataSource()
        repository = MoviesRepositoryImpl(remote, local)

        val result = repository.getMovieDetails(sampleMovie.title)

        Assert.assertNull(result)
    }



    class FakeLocalDataSource(
        var movies: MutableList<Movie> = mutableListOf(),
        var shouldThrow: Boolean = false
    ) : MoviesLocalDataSource {
        override fun isEmpty(): Boolean {
            if (shouldThrow) throw RuntimeException("Local Error")
            return movies.isEmpty()
        }

        override fun saveAll(movies: List<Movie>) {
            this.movies.addAll(movies)
        }

        override fun getAll(): List<Movie> = movies

        override fun clear() {
            movies.clear()
        }
    }

    class FakeRemoteDataSource(
        private val movies: List<Movie> = emptyList(),
        var shouldThrow: Boolean = false
    ) : MoviesRemoteDataSource {
        override suspend fun getPopularMovies(): List<Movie> {
            if (shouldThrow) throw RuntimeException("Remote Error")
            return movies
        }

        override suspend fun getMovieDetails(title: String): Movie {
            if (shouldThrow) throw RuntimeException("Remote Error")
            return movies.find { it.title == title } ?: throw RuntimeException("Movie not found")
        }
    }

    class FakeMoviesDetailBroker(
        private val tmdbMovie: Movie? = null,
        private val omdbMovie: Movie? = null
    ) : MoviesDetailExternalSource {

        override suspend fun getMovieByTitle(title: String): Movie? {
            return when {
                tmdbMovie != null && omdbMovie != null -> Movie(
                    id = tmdbMovie.id,
                    title = tmdbMovie.title,
                    overview = "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
                    releaseDate = tmdbMovie.releaseDate,
                    poster = tmdbMovie.poster,
                    backdrop = tmdbMovie.backdrop,
                    originalTitle = tmdbMovie.originalTitle,
                    originalLanguage = tmdbMovie.originalLanguage,
                    popularity = (tmdbMovie.popularity + omdbMovie.popularity) / 2.0,
                    voteAverage = (tmdbMovie.voteAverage + omdbMovie.voteAverage) / 2.0
                )
                tmdbMovie != null -> tmdbMovie.copy(overview = "TMDB: ${tmdbMovie.overview}")
                omdbMovie != null -> omdbMovie.copy(overview = "OMDB: ${omdbMovie.overview}")
                else -> null
            }
        }
    }

}