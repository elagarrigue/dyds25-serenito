package edu.dyds.movies.domain.usecase

import FakeMoviesRepository
import edu.dyds.movies.fakes.TestDataFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetMovieDetailUseCaseTest {

    private val fakeRepository = FakeMoviesRepository()
    private val useCase = GetMovieDetailUseCaseImpl(fakeRepository)

    @Test
    fun `invoke should return movie from repository`() = runBlocking {
        val movie = TestDataFactory.createMovie(1)
        fakeRepository.movieToReturn = movie

        val result = useCase.invoke(1)

        Assert.assertEquals(movie, result)
        Assert.assertEquals(1, fakeRepository.lastIdRequested)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runBlocking {
        fakeRepository.movieToReturn = null

        val result = useCase.invoke(999)

        Assert.assertNull(result)
        Assert.assertEquals(999, fakeRepository.lastIdRequested)
    }
}