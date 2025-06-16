import edu.dyds.movies.data.local.MoviesLocalDataSource
import edu.dyds.movies.data.local.MoviesLocalDataSourceImpl
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceTest {
    private lateinit var localDataSourceTest: MoviesLocalDataSource
    private lateinit var fakeMovie: Movie

    @Before
    fun setUp(){
        localDataSourceTest = MoviesLocalDataSourceImpl()
        fakeMovie = Movie(
            id = 1,
            title = "test movie",
            overview = "test overview",
            poster = "poster.jpg",
            backdrop = null,
            originalLanguage = "en",
            originalTitle = "Test movie original",
            popularity = 10.0,
            releaseDate = "2025-06-16",
            voteAverage = 8.0
        )
    }

    @Test
    fun `isEmpty return true when no movies are present`(){
        assertTrue(localDataSourceTest.isEmpty())
    }

    @Test
    fun `isEmpty return false when movies are present`(){
        localDataSourceTest.saveAll(listOf(fakeMovie))
        assertFalse(localDataSourceTest.isEmpty())
    }

    @Test
    fun `getAll movies return all saved movies`(){
        localDataSourceTest.saveAll(listOf(fakeMovie))
        val allMovies = localDataSourceTest.getAll()
        assertEquals(1, allMovies.size)
        assertEquals(fakeMovie, allMovies[0])
    }

    @Test
    fun `getFromId return the matching movie if exists`(){
        localDataSourceTest.saveAll(listOf(fakeMovie))
        val movie = localDataSourceTest.getFromId(1)
        assertNotNull(movie)
        assertEquals(fakeMovie, movie)
    }

    @Test
    fun `getFromId return null if the movie doesn't exist`(){
        val movie = localDataSourceTest.getFromId(999)
        assertNull(movie)
    }

    @Test
    fun `clear should clear all movies from cache`(){
        localDataSourceTest.saveAll(listOf(fakeMovie))
        localDataSourceTest.clear()
        assertTrue(localDataSourceTest.isEmpty())
    }
}