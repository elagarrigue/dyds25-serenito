package edu.dyds.movies.presentation.home

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import edu.dyds.movies.fakes.FakeSuccessPopularMoviesUseCase
import edu.dyds.movies.fakes.TestDataFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

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
    fun `getAllMovies should emit loading and data states`() = runTest {
        val movies = listOf(
            TestDataFactory.createQualifiedMovie(1),
            TestDataFactory.createQualifiedMovie(2)
        )
        val viewModel = HomeViewModel(FakeSuccessPopularMoviesUseCase(movies))

        val events = mutableListOf<HomeState>()

        val job = testScope.launch {
            viewModel.state.collect { state ->
                events.add(state)
            }
        }

        viewModel.getAllMovies()
        advanceUntilIdle()

        Assert.assertEquals(
            HomeState(isLoading = true, movies = emptyList()),
            events[0]
        )

        Assert.assertEquals(
            HomeState(isLoading = false, movies = movies),
            events[1]
        )

        job.cancel()
    }
}