package edu.dyds.movies.domain.usecase

import FakeMoviesRepository
import edu.dyds.movies.fakes.TestDataFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetPopularMoviesUseCaseTest {

    private val fakeRepository = FakeMoviesRepository()
    private val useCase = GetPopularMoviesUseCaseImpl(fakeRepository)

    @Test
    fun `invoke returns sorted and mapped qualified movies`() = runBlocking {
        val movies = listOf(
            TestDataFactory.createMovie("Title 1").copy(voteAverage = 5.9),
            TestDataFactory.createMovie("Title 2").copy(voteAverage = 8.0),
            TestDataFactory.createMovie("Title 3").copy(voteAverage = 6.0)
        )
        fakeRepository.popularMoviesToReturn = movies

        val result = useCase.invoke()

        Assert.assertTrue(fakeRepository.getPopularMoviesCalled)
        Assert.assertEquals(3, result.size)
        Assert.assertEquals("Title 2", result[0].movie.title)
        Assert.assertEquals("Title 3", result[1].movie.title)
        Assert.assertEquals("Title 1", result[2].movie.title)
        Assert.assertTrue(result[0].isGoodMovie)
        Assert.assertTrue(result[1].isGoodMovie)
        Assert.assertFalse(result[2].isGoodMovie)
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runBlocking {
        fakeRepository.popularMoviesToReturn = emptyList()

        val result = useCase.invoke()

        Assert.assertTrue(fakeRepository.getPopularMoviesCalled)
        Assert.assertTrue(result.isEmpty())
    }
}