import edu.dyds.movies.domain.usecase.GetMovieDetailUseCaseImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import utils.*

class GetMovieDetailUseCaseImplTest {

    private val fakeRepository = FakeMoviesRepository()
    private val useCase = GetMovieDetailUseCaseImpl(fakeRepository)

    @Test
    fun `invoke should return movie from repository`() = runBlocking {
        val movie = TestDataFactory.createMovie(1)
        fakeRepository.movieToReturn = movie

        val result = useCase.invoke(1)

        assertEquals(movie, result)
        assertEquals(1, fakeRepository.lastIdRequested)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runBlocking {
        fakeRepository.movieToReturn = null

        val result = useCase.invoke(999)

        assertNull(result)
        assertEquals(999, fakeRepository.lastIdRequested)
    }
}
