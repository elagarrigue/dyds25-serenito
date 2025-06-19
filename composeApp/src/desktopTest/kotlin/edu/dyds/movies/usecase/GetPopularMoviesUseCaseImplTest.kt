package edu.dyds.movies.usecase

import FakeMoviesRepository
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCaseImpl
import edu.dyds.movies.fakes.TestDataFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetPopularMoviesUseCaseImplTest {

    private val fakeRepository = FakeMoviesRepository()
    private val useCase = GetPopularMoviesUseCaseImpl(fakeRepository)

    @Test
    fun `invoke returns sorted and mapped qualified movies`() = runBlocking {
        val movies = listOf(
            TestDataFactory.createMovie(1).copy(voteAverage = 5.9),
            TestDataFactory.createMovie(2).copy(voteAverage = 8.0),
            TestDataFactory.createMovie(3).copy(voteAverage = 6.0)
        )
        fakeRepository.popularMoviesToReturn = movies

        val result = useCase.invoke()

        Assert.assertTrue(fakeRepository.getPopularMoviesCalled)
        Assert.assertEquals(3, result.size)
        Assert.assertEquals(2, result[0].movie.id)
        Assert.assertEquals(3, result[1].movie.id)
        Assert.assertEquals(1, result[2].movie.id)
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