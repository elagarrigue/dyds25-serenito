package edu.dyds.movies.presentation.home

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import edu.dyds.movies.fakes.FakeSuccessPopularMoviesUseCase
import edu.dyds.movies.fakes.TestDataFactory

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @Test
    fun `initial state has to be empty list and loading`() {
        val viewModel = HomeViewModel(FakeSuccessPopularMoviesUseCase(emptyList()))
        val state = viewModel.state.value
        Assert.assertTrue(state.isLoading)
        Assert.assertTrue(state.movies.isEmpty())
    }

    @Test
    fun `when getAllMovies succeeds, the state is updated with the movies`() = runTest {
        val movies = listOf(
            TestDataFactory.createQualifiedMovie(1),
            TestDataFactory.createQualifiedMovie(2)
        )
        val viewModel = HomeViewModel(FakeSuccessPopularMoviesUseCase(movies))
        viewModel.getAllMovies()
        val state = viewModel.state.first { !it.isLoading }
        Assert.assertEquals(movies, state.movies)
    }

}