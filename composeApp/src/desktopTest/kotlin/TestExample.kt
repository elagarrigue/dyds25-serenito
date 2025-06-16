import androidx.lifecycle.ViewModel
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.home.HomeState
import edu.dyds.movies.presentation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.*

class TestExample {

    @OptIn(ExperimentalCoroutinesApi::class)
    val testScope = CoroutineScope(UnconfinedTestDispatcher())

    interface MyService {
        fun getData(): String
        fun sideEffect()
    }

    class MyViewModel(private val myService: MyService) : ViewModel() {
        val dataFlow = MutableStateFlow("")

        fun getData(): String {
            myService.sideEffect()
            return myService.getData()
        }
    }

    class MyServiceFake : MyService {
        var isSideEffectCalled = false

        override fun getData(): String {
            return "Data"
        }

        override fun sideEffect() {
            isSideEffectCalled = true
        }
    }

    class FakeGetAllMoviesUseCase : GetPopularMoviesUseCase {
        override suspend fun invoke(): List<QualifiedMovie> {
            val movie = Movie(
                id = 1,
                title = "Test Movie",
                overview = "Test Overview",
                poster = "poster.jpg",
                backdrop = null,
                originalLanguage = "en",
                originalTitle = "Original Title",
                popularity = 10.0,
                releaseDate = "2024-01-01",
                voteAverage = 8.0
            )
            return listOf(QualifiedMovie(movie = movie, isGoodMovie = true))
        }
    }

    @Test
    fun `get data should return data and side effect should be triggered`() {
        // arrange
        val myService = MyServiceFake()
        val myViewModel = MyViewModel(myService)

        // act
        val result = myViewModel.getData()

        // assert
        assert(result == "Data")
        assert(myService.isSideEffectCalled)
    }

    @Test
    fun `data flow should emit string events`() = runTest {
        val myService = MyServiceFake()
        val myViewModel = MyViewModel(myService)

        val events: ArrayList<String> = arrayListOf()
        testScope.launch {
            myViewModel.dataFlow.collect { events.add(it) }
        }

        // act
        myViewModel.dataFlow.emit("value")
        myViewModel.dataFlow.emit("value2")

        // assert
        assert(events.last() == "value2")
    }

    @Test
    fun `getAllMovies should update state with movies`() = runTest {
        // Arrange
        val useCase = FakeGetAllMoviesUseCase()
        val viewModel = HomeViewModel(useCase)

        // Act
        viewModel.getAllMovies()

        // Assert
        val state = viewModel.state.first { !it.isLoading }
        assertFalse(state.isLoading)
        assertEquals(1, state.movies.size)
        assertEquals("Test Movie", state.movies.first().movie.title)
        assertTrue(state.movies.first().isGoodMovie)
    }

    @Test
    fun `state should emit loading then loaded`() = runTest {
        val useCase = FakeGetAllMoviesUseCase()
        val viewModel = HomeViewModel(useCase)

        val emissions = mutableListOf<HomeState>()
        val job = launch {
            viewModel.state.collect { emissions.add(it) }
        }

        viewModel.getAllMovies()

        // Assert loading and loaded states
        assertTrue(emissions.first().isLoading)
        assertFalse(emissions.last().isLoading)

        job.cancel()
    }


    @Test
    fun `getPopularMovies returns movie list`() = runTest {

    }

    @Test
    fun `getMovieDetails returns movie that isn´t null`() = runTest {

    }
}