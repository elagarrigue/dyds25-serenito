package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.fakes.TestDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceTest {

    private lateinit var localDataSource: MoviesLocalDataSource
    private lateinit var fakeMovie: Movie

    @Before
    fun setUp() {
        localDataSource = MoviesLocalDataSourceImpl()
        fakeMovie = TestDataFactory.createMovie("Title 1")
    }

    @Test
    fun `isEmpty returns true when no movies are present`() = runTest {
        assertTrue(localDataSource.isEmpty())
    }

    @Test
    fun `isEmpty returns false when movies are present`() = runTest {
        localDataSource.saveAll(listOf(fakeMovie))
        assertFalse(localDataSource.isEmpty())
    }

    @Test
    fun `getAll returns all saved movies`() = runTest {
        localDataSource.saveAll(listOf(fakeMovie))
        val allMovies = localDataSource.getAll()
        assertEquals(1, allMovies.size)
        assertEquals(fakeMovie, allMovies[0])
    }

    @Test
    fun `clear clears all movies from cache`() = runTest {
        localDataSource.saveAll(listOf(fakeMovie))
        localDataSource.clear()
        assertTrue(localDataSource.isEmpty())
    }
}