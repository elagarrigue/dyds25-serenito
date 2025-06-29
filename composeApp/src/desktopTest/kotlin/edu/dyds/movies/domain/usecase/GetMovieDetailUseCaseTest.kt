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
        val movie = TestDataFactory.createMovie("Title 1")
        fakeRepository.movieToReturn = movie

        val result = useCase.invoke("TestFilm")

        Assert.assertEquals(movie, result)
        Assert.assertEquals("TestFilm", fakeRepository.lastTitleRequested)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runBlocking {
        fakeRepository.movieToReturn = null

        val result = useCase.invoke("NoFilm")

        Assert.assertNull(result)
        Assert.assertEquals("NoFilm", fakeRepository.lastTitleRequested)
    }
}