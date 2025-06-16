import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @Test
    fun `state inicial tiene que ser una lista vacia y no cargando`() {
        val viewModel = HomeViewModel(FakeSuccessPopularMoviesUseCase(emptyList()))
        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertTrue(state.movies.isEmpty())
    }

    @Test
    fun `cuando getAllMovies es exitoso, state se actualiza con las peliculas`() = runTest {
        val movies = listOf(createQualifiedMovie(1), createQualifiedMovie(2))
        val viewModel = HomeViewModel(FakeSuccessPopularMoviesUseCase(movies))

        viewModel.getAllMovies()

        val state = viewModel.state.first { !it.isLoading }
        assertEquals(movies, state.movies)
        assertFalse(state.isLoading)
    }

    @Test
    fun `cuando getAllMovies lanza excepcion, state vuelve a no cargando y lista vacia`() = runTest {
        val viewModel = HomeViewModel(FakeFailingPopularMoviesUseCase())

        viewModel.getAllMovies()

        val state = viewModel.state.first { !it.isLoading }
        assertFalse(state.isLoading)
        assertTrue(state.movies.isEmpty())
    }

    // Fake que siempre devuelve una película exitosa
    private class FakeSuccessPopularMoviesUseCase(
        private val movies: List<QualifiedMovie>
    ) : GetPopularMoviesUseCase {
        override suspend fun invoke(): List<QualifiedMovie> = movies
    }

    // Fake que simula una excepcion
    private class FakeFailingPopularMoviesUseCase : GetPopularMoviesUseCase {
        override suspend fun invoke(): List<QualifiedMovie> {
            throw RuntimeException("Simulated failure")
        }
    }

    // Generador de QualifiedMovie fake
    private fun createQualifiedMovie(id: Int, isGood: Boolean = true): QualifiedMovie {
        return QualifiedMovie(
            movie = createMovie(id),
            isGoodMovie = isGood
        )
    }

    // Generador de películas fake
    private fun createMovie(id: Int): Movie {
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