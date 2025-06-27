package edu.dyds.movies.presentation.detail

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import edu.dyds.movies.fakes.FakeSuccessDetailUseCase
import edu.dyds.movies.fakes.TestDataFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getMovieDetail succeeds, the state emits loading and then data`() = runTest {
        val movie = TestDataFactory.createMovie(1)
        val viewModel = DetailViewModel(FakeSuccessDetailUseCase(movie))

        val events = mutableListOf<DetailState>()

        val job = testScope.launch {
            viewModel.state.collect { state ->
                events.add(state)
            }
        }

        viewModel.getMovieDetail("TestFilm")
        advanceUntilIdle()

        assertEquals(
            DetailState(isLoading = true, movie = null),
            events[0]
        )

        assertEquals(
            DetailState(isLoading = false, movie = movie),
            events[1]
        )

        job.cancel()
    }
}

