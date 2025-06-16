import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.MoviesRemoteDataSource
import edu.dyds.movies.data.local.MoviesLocalDataSource
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryTest {
    
    @Test
    fun `getPopularMovies returns local data if not empty`() = runTest {
        val local = FakeLocalDataSource(movies = mutableListOf(sampleMovie))
        val remote = FakeRemoteDataSource()
        val repo = MoviesRepositoryImpl(remote, local)

        val result = repo.getPopularMovies()

        assertEquals(1, result.size)
        assertEquals("Test Movie", result.first().title)
    }

    @Test
    fun `getPopularMovies fetches from remote if local is empty`() = runTest {
        val local = FakeLocalDataSource()
        val remote = FakeRemoteDataSource(movies = listOf(sampleMovie))
        val repo = MoviesRepositoryImpl(remote, local)

        val result = repo.getPopularMovies()

        assertEquals(1, result.size)
        assertEquals("Test Movie", result.first().title)
        assertEquals(1, local.getAll().size)
    }

    @Test
    fun `getPopularMovies returns empty list on error`() = runTest {
        val local = FakeLocalDataSource(shouldThrow = true)
        val remote = FakeRemoteDataSource()
        val repo = MoviesRepositoryImpl(remote, local)

        val result = repo.getPopularMovies()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMovieDetails returns local movie if present`() = runTest {
        val local = FakeLocalDataSource(movies = mutableListOf(sampleMovie))
        val remote = FakeRemoteDataSource()
        val repo = MoviesRepositoryImpl(remote, local)

        val result = repo.getMovieDetails(1)

        assertNotNull(result)
        assertEquals("Test Movie", result?.title)
    }

    @Test
    fun `getMovieDetails returns remote movie if not found locally`() = runTest {
        val local = FakeLocalDataSource()
        val remote = FakeRemoteDataSource(movies = listOf(sampleMovie))
        val repo = MoviesRepositoryImpl(remote, local)

        val result = repo.getMovieDetails(1)

        assertNotNull(result)
        assertEquals("Test Movie", result?.title)
    }

    @Test
    fun `getMovieDetails returns null on error`() = runTest {
        val local = FakeLocalDataSource(shouldThrow = true)
        val remote = FakeRemoteDataSource()
        val repo = MoviesRepositoryImpl(remote, local)

        val result = repo.getMovieDetails(1)

        assertNull(result)
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

        override fun getAll(): List<Movie> {
            return movies
        }

        override fun getFromId(id: Int): Movie? {
            return movies.find { it.id == id }
        }
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

        override suspend fun getMovieDetails(id: Int): Movie {
            if (shouldThrow) throw RuntimeException("Remote Error")
            return movies.find { it.id == id } ?: throw RuntimeException("Movie not found")
        }
    }

    private val sampleMovie = Movie(
        id = 1,
        title = "Test Movie",
        overview = "Overview",
        poster = "poster.jpg",
        backdrop = null,
        originalLanguage = "en",
        originalTitle = "Original",
        popularity = 10.0,
        releaseDate = "2024-01-01",
        voteAverage = 8.0
    )
}