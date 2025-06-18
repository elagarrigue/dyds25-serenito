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
    fun `initial state has to be empty list and loading`() {
        val viewModel = HomeViewModel(FakeSuccessPopularMoviesUseCase(emptyList()))
        val state = viewModel.state.value
        assertTrue(state.isLoading)
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

}
