import edu.dyds.movies.presentation.detail.DetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Test
import utils.*

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @Test
    fun `state inicial tiene que ser vacio y no cargando`() {
        val viewModel = DetailViewModel(FakeSuccessDetailUseCase())
        val state = viewModel.state.value
        assertTrue(state.isLoading)
        assertNull(state.movie)
    }

    @Test
    fun `cuando getMovieDetail es exitoso, state se actualiza con la pelicula`() = runTest {
        val movie = TestDataFactory.createMovie(1)
        val viewModel = DetailViewModel(FakeSuccessDetailUseCase(movie))
        viewModel.getMovieDetail(1)
        val state = viewModel.state.first { !it.isLoading }
        assertEquals(movie, state.movie)
    }

}
