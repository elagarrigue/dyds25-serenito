import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.GetMovieDetailUseCase
import edu.dyds.movies.presentation.detail.DetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @Test
    fun `state inicial tiene que ser vacio y no cargando`() {
        val viewModel = DetailViewModel(FakeSuccessUseCase())
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.movie)
    }

    @Test
    fun `cuando getMovieDetail es exitoso, state se actualiza con la pelicula`() = runTest {
        val movie = createMovie(1)
        val viewModel = DetailViewModel(FakeSuccessUseCase(movie))

        viewModel.getMovieDetail(1)

        val state = viewModel.state.first { !it.isLoading }
        assertEquals(movie, state.movie)
    }

    @Test
    fun `cuando getMovieDetail lanza excepcion, state debe mantener movie en null`() = runTest {
        val viewModel = DetailViewModel(FakeFailingUseCase())

        viewModel.getMovieDetail(1)

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.movie)
    }

    // Fake que siempre devuelve una película exitosa
    private class FakeSuccessUseCase(private val movie: Movie = createMovie(42)) : GetMovieDetailUseCase {
        override suspend fun invoke(id: Int): Movie? = movie
    }

    // Fake que simula una excepcion
    private class FakeFailingUseCase : GetMovieDetailUseCase {
        override suspend fun invoke(id: Int): Movie? {
            throw RuntimeException("Simulated failure")
        }
    }

    // Generador de películas fake
    companion object {
        fun createMovie(id: Int): Movie {
            return Movie(
                id = id,
                title = "Title $id",
                overview = "Overview $id",
                releaseDate = "2023-01-01",
                poster = "poster_$id",
                backdrop = null,
                originalTitle = "Original Title $id",
                originalLanguage = "en",
                popularity = 10.0,
                voteAverage = 8.0
            )
        }
    }
}
