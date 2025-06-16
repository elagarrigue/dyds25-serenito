import edu.dyds.movies.presentation.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import utils.*

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
        val movies = listOf(
            TestDataFactory.createQualifiedMovie(1),
            TestDataFactory.createQualifiedMovie(2)
        )
        val viewModel = HomeViewModel(FakeSuccessPopularMoviesUseCase(movies))
        viewModel.getAllMovies()
        val state = viewModel.state.first { !it.isLoading }
        assertEquals(movies, state.movies)
    }

    @Test
    fun `cuando getAllMovies lanza excepcion, state vuelve a no cargando y lista vacia`() = runTest {
        val viewModel = HomeViewModel(FakeFailingPopularMoviesUseCase())
        viewModel.getAllMovies()
        val state = viewModel.state.first { !it.isLoading }
        assertFalse(state.isLoading)
        assertTrue(state.movies.isEmpty())
    }
}
